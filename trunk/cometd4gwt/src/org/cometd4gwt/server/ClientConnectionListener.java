package org.cometd4gwt.server;

public interface ClientConnectionListener {
	void onConnect(String clientId);
	void onDisconnect(String clientId);
}
