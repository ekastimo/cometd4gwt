package org.cometd4gwt.server;

import java.util.Map;

import org.cometd.Bayeux;
import org.cometd.Client;
import org.cometd.server.BayeuxService;
import org.cometd4gwt.client.CometMessageConsumer;
import org.cometd4gwt.client.CometdConstants;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BayeuxSubscriber extends BayeuxService implements CometdConstants {
	private final CometMessageConsumer consumer;

	public BayeuxSubscriber(Bayeux bayeux, String channelId, CometMessageConsumer consumer) {
		super(bayeux, "");
		this.consumer = consumer;

		subscribe(channelId, "onMessage");
	}

	public void onMessage(Client remote, Map<String, Object> data) {
		consumer.onMessageReceived((IsSerializable) data.get(OBJECT_MESSAGE));
	}
}