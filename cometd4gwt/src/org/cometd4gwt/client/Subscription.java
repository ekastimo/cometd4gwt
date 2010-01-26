package org.cometd4gwt.client;

public class Subscription extends CometdJso {
	protected Subscription() {
	}

	public final native String getSubscription()/*-{
		return this.subscription;
	}-*/;

	// @Override
	public final String _toString() {
		return "Subscription [getSubscription()=" + getSubscription() + ", getChannel()=" + getChannel() + ", getId()="
				+ getId() + ", isSuccessful()=" + isSuccessful() + "]";
	}
}