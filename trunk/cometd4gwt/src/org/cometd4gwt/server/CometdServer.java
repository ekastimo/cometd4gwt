package org.cometd4gwt.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

	private List<ClientConnectionListener> clientConnectionListeners = new ArrayList<ClientConnectionListener>();
	private Map<String, Client> clients = new HashMap<String, Client>();
	private Set<String> connectedClientIds = new HashSet<String>(); //Collections.synchronizedSet();

	public CometdServer(Bayeux bayeux) {
		super(bayeux, "");

		subscribe("/meta/connect", "onConnectMessage");
		subscribe("/meta/disconnect", "onDisconnectMessage");

//		subscribe("/meta/*", "test");
	}

	public void test(Client client, Message message) {
		System.err.println("server-meta-message: " + message);
	}

	public void onConnectMessage(final Client client, Message message) {
		// This is a rocket
		if (Serializer.isSerializationPolicyNull()) {
			client.deliver(getClient(), GENERATE_SERIALIZATION_POLICY_REQUEST, new HashMap<String, Object>(), null);
			System.err.println("serializationPolicy=null");
		}

		//  
		else if (!connectedClientIds.contains(client.getId())) {
			synchronized (connectedClientIds) {
				if (!connectedClientIds.contains(client.getId())) {
					connectedClientIds.add(client.getId());

					final String userId = getBayeux().getCurrentRequest().getHeader("requestHeader");
					client.addListener(new RemoveListener() {
						@Override
						public void removed(String clientId, boolean timeout) {
							System.out.println("client.removed(" + clientId + ", " + timeout + ")");
							onDisconnect(client, userId);
						}
					});

					for (ClientConnectionListener ccl : clientConnectionListeners) {
						ccl.onConnect(client, userId);
					}
				}
			}
		}
	}

	public void addClient(Client client, String userId) {
		clients.put(client.getId(), client);
		for (ClientConnectionListener ccl : clientConnectionListeners) {
			ccl.onConnect(client, userId);
		}
	}

	public void onDisconnectMessage(Client client, Message message) {
		if (connectedClientIds.contains(client.getId())) {
			synchronized (connectedClientIds) {
				if (connectedClientIds.remove(client.getId())) {
					for (ClientConnectionListener ccl : clientConnectionListeners) {
						final String userId = getBayeux().getCurrentRequest().getHeader("requestHeader");
						ccl.onDisconnect(client, userId);
					}
				}
			}
		}
	}

	public void onDisconnect(Client client, String requestHeader) {
		if (connectedClientIds.contains(client.getId())) {
			synchronized (connectedClientIds) {
				if (connectedClientIds.remove(client.getId())) {
					for (ClientConnectionListener ccl : clientConnectionListeners) {
						ccl.onDisconnect(client, requestHeader);
					}
				}
			}
		}
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

		System.err.println("isSubscribed(" + channelId + ", " + clientId + ") = false");
		return false;
	}

	public void diconnect(String clientId) {
//		Client client = getBayeux().getClient(clientId);
		Client client = getClient(clientId);
		
		System.out.println("cometdServer.disconnect(clientId=" + clientId + ", client=" + client + ")");

		if (client != null) {
			client.disconnect();
		}
	}

	public void subscribeChannel(String channelId, CometMessageConsumer consumer) {
		System.out.println("subscribing channel=" + channelId);
		getBayeux().getChannel(channelId, true);
		new BayeuxSubscriber(getBayeux(), channelId, consumer);
	}

	public void removeChannel(String channelId) {
		Channel channel = getBayeux().getChannel(channelId, false);
		if (channel != null) {
			channel.remove();
		}
	}
}