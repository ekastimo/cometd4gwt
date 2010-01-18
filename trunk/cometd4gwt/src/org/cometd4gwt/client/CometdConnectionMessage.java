package org.cometd4gwt.client;

public class CometdConnectionMessage extends CometdMessage {
	protected CometdConnectionMessage() {
	}
	
	public final boolean isConnected() {
		return isSuccessful();
	}
}
