package org.cometd4gwt.client.impl;


class CometdConnectionMessage extends CometdMetaMessage {
	protected CometdConnectionMessage() {
	}

	public final boolean isConnected() {
		return isSuccessful();
	}
}
