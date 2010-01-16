package org.cometd4gwt.client;

import java.util.HashMap;
import java.util.Map;

public class SubscriptionListener implements MessageListener<Subscription> {

	private Map<String, MessageListener<Subscription>> disposableReceivers = new HashMap<String, MessageListener<Subscription>>();

	@Override
	public void onMessageReceived(Subscription subscription) {
		MessageListener<Subscription> listener = disposableReceivers.remove(subscription.getSubscription());
		if (listener != null) {
			listener.onMessageReceived(subscription);
		}
	}

	public void addDisposableListener(String channel, MessageListener<Subscription> listener) {
		disposableReceivers.put(channel, listener);
	}
}