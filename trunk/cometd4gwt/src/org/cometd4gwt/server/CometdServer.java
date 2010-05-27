package org.cometd4gwt.server;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cometd.Bayeux;
import org.cometd.Channel;
import org.cometd.Client;
import org.cometd.Message;
import org.cometd.RemoveListener;
import org.cometd.server.BayeuxService;
import org.cometd4gwt.client.CometMessageConsumer;
import org.cometd4gwt.client.CometdConstants;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CometdServer extends BayeuxService implements CometdConstants {
	public static final String ATTRIBUTE = "org.cometd4gwt.CometdServer";

	/**
	 * Interested party that want to be notified of user's session.
	 */
	private ClientConnectionListeners clientConnectionListeners = new ClientConnectionListeners();

	/**
	 * List of connected client IDs to avoid repeated event fire on reconnect on
	 * a long-polling
	 */
	private Set<String> connectedClientIds = Collections.synchronizedSet(new HashSet<String>());

	/**
	 * Clients that is being added from the server side to receive message like
	 * any other client. This clients imitate the browser side client without
	 * any hassle of connection
	 */
	private Map<String, Client> clients = new HashMap<String, Client>();

	/**
	 * On any restart server will be assigned a unique ID to be synced with the
	 * client
	 */
	private String serverVersion = "" + new Date().getTime();

	public Map<String, Object> getServerVertion() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("version", serverVersion);
		return map;
	}

	public CometdServer(Bayeux bayeux) {
		super(bayeux, "");
		subscribe("/meta/connect", "onConnectMessage");
	}

	public void onMetaMessage(final Client client, Message message) {
		System.err.println("Meta Message=" + message);
	}

	public void onConnectMessage(final Client client, Message message) {

		/**
		 * The first client on the very first attempt to connect will receive a
		 * request to generate the serialization policy by calling a simple
		 * service in the MetaSerice. All other clients will use that policy to
		 * serialize the outgoing objects
		 */
		if (Serializer.isSerializationPolicyNull()) {
			client.deliver(getClient(), GENERATE_SERIALIZATION_POLICY_REQUEST, new HashMap<String, Object>(), null);
			System.err.println("serializationPolicy=null");
		}

		/**
		 * We are only interested on the fist connect message. All other
		 * subsequent messages are the result of long-polling.
		 */
		else if (!connectedClientIds.contains(client.getId())) {
			connectedClientIds.add(client.getId());

			// Send client the current version, if client needs to be synced  
			client.deliver(client, SERVER_VERSION_CHANNEL, getServerVertion(), null);

			final String userId = getCurrentUserId();

			// Ensure, on disconnect the resources will be cleared
			client.addListener(new RemoveListener() {
				@Override
				public void removed(String clientId, boolean timeout) {
					if (connectedClientIds.remove(client.getId())) {
						clientConnectionListeners.onDisconnect(client, userId);
					}
				}
			});

			// Fire events
			clientConnectionListeners.onConnect(client, userId);
		}
	}

	public void diconnect(String clientId) {
		Client client = getClient(clientId);
		if (client != null) {
			client.disconnect();
		}
	}

	/**
	 * This method will be called from the server side
	 * 
	 * @param client
	 * @param userId
	 */
	public void addClient(Client client, String userId) {
		clients.put(client.getId(), client);
		clientConnectionListeners.onConnect(client, userId);
	}

	public void addClientConnectionListener(ClientConnectionListener listener) {
		clientConnectionListeners.add(listener);
	}

	public boolean publish(String channelId, IsSerializable message) {
		Channel channel = getBayeux().getChannel(channelId, true);

		if (channel == null) {
			System.err.println("WARNNING: getBayeux().getChannel(" + channelId + ", true)=null, channelId=" + message
					+ ", message=" + message);
		} else {
			channel.publish(null, new JsonMap(message), null);
		}

		return channel != null;
	}

	private Client getClient(String clientId) {
		Client client = getBayeux().getClient(clientId);
		return client == null ? clients.get(clientId) : client;
	}

	public boolean publish(String channelId, IsSerializable message, String clientId) {
//		Client client = getBayeux().getClient(clientId);
		Client client = getClient(clientId);

//		System.err.println(this + "-publish(" + channelId + ", " + message + ", " + clientId + "), client=" + client);

		if (client == null) {
			System.err.println(this + "-WARNNING: getBayeux().getClient(" + clientId + ")=null, channelId=" + message
					+ ", message=" + message);
		} else {
			client.deliver(null, channelId, new JsonMap(message), null);
		}

		return client != null;
	}

	public boolean isSubscribed(String channelId, String clientId) {
		Channel channel = getBayeux().getChannel(channelId, false);
		if (channel != null) {
			Client client = getBayeux().getClient(clientId);
			if (client != null) {
				return channel.getSubscribers().contains(client);
			}
		}

//		System.err.println("isSubscribed(" + channelId + ", " + clientId + ") = false");
		return false;
	}

	public void subscribeChannel(String channelId, CometMessageConsumer consumer) {
//		System.out.println("subscribing channel=" + channelId);
		getBayeux().getChannel(channelId, true);
		new BayeuxSubscriber(getBayeux(), channelId, consumer);
	}

	public void removeChannel(String channelId) {
		Channel channel = getBayeux().getChannel(channelId, false);
		if (channel != null) {
			channel.remove();
		}
	}

	private String getCurrentUserId() {
		return getBayeux().getCurrentRequest().getHeader("requestHeader");
	}
}