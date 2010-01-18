package org.cometd4gwt.client;

import java.util.HashMap;
import java.util.Map;

public class SubscriptionListener implements JsoListener<Subscription> {

	private Map<String, JsoListener<Subscription>> disposableReceivers = new HashMap<String, JsoListener<Subscription>>();

	@Override
	public void onMessageReceived(Subscription subscription) {
		JsoListener<Subscription> listener = disposableReceivers.remove(subscription.getSubscription());
		if (listener != null) {
			listener.onMessageReceived(subscription);
		}
	}

	public void addDisposableListener(String channel, JsoListener<Subscription> listener) {
		disposableReceivers.put(channel, listener);
	}
}