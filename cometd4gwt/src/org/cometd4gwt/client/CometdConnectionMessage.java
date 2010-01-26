package org.cometd4gwt.client;

public class CometdConnectionMessage extends CometdJso {
	protected CometdConnectionMessage() {
	}
	
	public final boolean isConnected() {
		return isSuccessful();
	}
}
