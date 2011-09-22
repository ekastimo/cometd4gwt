package org.cometd4gwt.client.impl;


public class Handshake extends CometdMetaMessage {
	protected Handshake() {
	}

	public final native String getClientId()/*-{
		return this.clientId;
	}-*/;
}