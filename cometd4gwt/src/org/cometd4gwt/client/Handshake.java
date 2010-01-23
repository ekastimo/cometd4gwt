package org.cometd4gwt.client;

public class Handshake extends CometdMessage {
	protected Handshake() {
	}

	public final native String getClientId()/*-{
		return this.clientId;
	}-*/;
}