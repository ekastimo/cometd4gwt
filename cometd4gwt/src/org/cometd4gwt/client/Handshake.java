package org.cometd4gwt.client;

public class Handshake extends CometdJso {
	protected Handshake() {
	}

	public final native String getClientId()/*-{
		return this.clientId;
	}-*/;
}