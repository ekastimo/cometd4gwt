package org.cometd4gwt.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.cometd.Client;
import org.cometd.ClientListener;
import org.cometd.Extension;
import org.cometd.Message;
import org.cometd4gwt.client.CometMessageConsumer;
import org.cometd4gwt.client.CometdConstants;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ServerSideClient implements Client, CometdConstants {

	private String id;
	private Map<String, CometMessageConsumer> consumers = new HashMap<String, CometMessageConsumer>();

	public ServerSideClient() {
		this.id = "ServerSideClient.id" + System.nanoTime();
	}

	@Override
	public String getId() {
		return id;
	}

	public void listenChannel(String topic, CometMessageConsumer cometMessageConsumer) {
		if (consumers.get(topic) != null) {
			throw new RuntimeException("consumer for topic already exist");
		}
		consumers.put(topic, cometMessageConsumer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deliver(Client from, String toChannel, Object data, String id) {
		CometMessageConsumer cometMessageConsumer = consumers.get(toChannel);
		if (cometMessageConsumer != null) {
			HashMap<String, Object> map = (HashMap<String, Object>) data;
			cometMessageConsumer.onMessageReceived((IsSerializable) map.get(OBJECT_MESSAGE));
		}
	}

	// NO need ----------------------------------------------------------------------------

	@Override
	public void addExtension(Extension ext) {
	}

	@Override
	public void addListener(ClientListener listener) {
	}

	@Override
	public void disconnect() {
	}

	@Override
	public void endBatch() {
	}

	@Override
	public int getMaxQueue() {
		return 0;
	}

	@Override
	public Queue<Message> getQueue() {
		return null;
	}

	@Override
	public boolean hasMessages() {
		return false;
	}

	@Override
	public boolean isLocal() {
		return false;
	}

	@Override
	public void removeExtension(Extension ext) {
	}

	@Override
	public void removeListener(ClientListener listener) {
	}

	@Override
	public void setMaxQueue(int max) {
	}

	@Override
	public void startBatch() {
	}

	@Override
	public List<Message> takeMessages() {
		return null;
	}
}