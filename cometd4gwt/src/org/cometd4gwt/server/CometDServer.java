package org.cometd4gwt.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cometd.Bayeux;
import org.cometd.Channel;
import org.cometd.Client;
import org.cometd.Message;
import org.cometd.server.BayeuxService;
import org.cometd4gwt.client.CometConstants;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CometDServer extends BayeuxService implements CometConstants {
	public static final String ATTRIBUTE = "org.cometd4gwt.CometServer";

	private List<ClientConnectionListener> clientConnectionListeners = new ArrayList<ClientConnectionListener>();

	public CometDServer(Bayeux bayeux) {
		super(bayeux, "");

		subscribe("/meta/connect", "onConnect");
		subscribe("/meta/disconnect", "onDisconnect");

		subscribe("/meta/*", "test");
	}

	public void test(Client client, Message message) {
		System.err.println("meta-message: " + message);
	}

	// FIXME - be sure that when a client connects for the first time, the message id is 2
	public void onConnect(Client client, Message message) {
		if (message.getId().equals("2")) {
			for (ClientConnectionListener ccl : clientConnectionListeners) {
				ccl.onConnect(client, getBayeux().getCurrentRequest());
			}
		}
	}

	public void onDisconnect(Client client, Message message) {
		for (ClientConnectionListener ccl : clientConnectionListeners) {
			ccl.onDisconnect(client, getBayeux().getCurrentRequest());
		}
	}

	public void addClientConnectionListener(ClientConnectionListener listener) {
		clientConnectionListeners.add(listener);
	}

	public void publish(String channelId, IsSerializable message) {
		Channel channel = getBayeux().getChannel(channelId, true);

		if (channel == null) {
			System.err.println("WARNNING: getBayeux().getChannel(" + channelId + ", true)=null, channelId=" + message
					+ ", message=" + message);
		} else {
			channel.publish(null, toMap(message), null);
		}
	}

	public void publish(String channelId, IsSerializable message, String clientId) {
		Client client = getBayeux().getClient(clientId);
		if (client == null) {
			System.err.println("WARNNING: getBayeux().getClient(" + clientId + ")=null, channelId=" + message
					+ ", message=" + message);
		} else {
			client.deliver(null, channelId, toMap(message), null);
		}
	}

	private final Map<String, Object> toMap(IsSerializable message) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(SERIALIZED_STRING, ServerSerializer.toString(message));

		return map;
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

//	public void setChannelPersistent(String channelId, boolean persistent) {
//		getBayeux().getChannel(channelId, true).setPersistent(persistent);
//	}
//	
//	private void unsubscribeChannel(String channelId, String clientId) {
//		Channel channel = getBayeux().getChannel(channelId, false);
//		if (channel != null) {
//			Client client = getBayeux().getClient(clientId);
//			if (client != null) {
//				channel.unsubscribe(client);
//			}
//		}
//	}
//
//	private void subscribeChannel(String channelId, String clientId) {
//		Channel channel = getBayeux().getChannel(channelId, false);
//		if (channel != null) {
//			Client client = getBayeux().getClient(clientId);
//			if (client != null) {
//				channel.subscribe(client);
//			}
//		}
//	}
}