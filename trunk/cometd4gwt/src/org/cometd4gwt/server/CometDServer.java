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

	private List<ClientConnectionListener> ccls = new ArrayList<ClientConnectionListener>();

	public CometDServer(Bayeux bayeux) {
		super(bayeux, "hello");

		subscribe("/twitter", "onTest");
		
		subscribe("/meta/connect", "onConnect");
		subscribe("/meta/disconnect", "onDisconnect");
	}

	public void onTest(Client remote, Message message) {
		System.err.println("remote=" + remote + ", message=" + message);
	}

	public void onConnect(Client remote, Message message) {
		// FIXME - be sure that when a client connects for the first time, the message id is 2
//		System.err.println("connection message=" + message);
		if (message.getId().equals("2")) {
			System.err.println("connection message=" + message);
			for (ClientConnectionListener l : ccls) {
				l.onConnect(message.getClientId());
			}
		}
	}

	public void onDisconnect(Client remote, Message message) {
//		System.err.println("Disconnection message=" + message);
		for (ClientConnectionListener l : ccls) {
			l.onDisconnect(message.getClientId());
		}
	}

	public void addClientConnectionListener(ClientConnectionListener listener) {
		ccls.add(listener);
	}

	public void setChannelPersistent(String channelId, boolean persistent) {
		getBayeux().getChannel(channelId, true).setPersistent(persistent);
	}

	public void unsubscribeChannel(String channelId, String clientId) {
		Channel channel = getBayeux().getChannel(channelId, false);
		if (channel != null) {
			Client client = getBayeux().getClient(clientId);
			if (client != null) {
				channel.unsubscribe(client);
			}
		}
	}

	public void subscribeChannel(String channelId, String clientId) {
		Channel channel = getBayeux().getChannel(channelId, false);
		if (channel != null) {
			Client client = getBayeux().getClient(clientId);
			if (client != null) {
				channel.subscribe(client);
			}
		}
	}

	public void publish(String channelId, IsSerializable message) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(SERIALIZED_STRING, ServerSerializer.toString(message));
		getBayeux().getChannel(channelId, true).publish(null, map, null);
	}
}