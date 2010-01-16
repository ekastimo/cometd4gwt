package org.cometd4gwt.client;

public class CometdConnectionMessage extends CometdMetaMessage {
	protected CometdConnectionMessage() {
	}
	
	public final boolean isConnected() {
		return isSuccessful();
	}
}
