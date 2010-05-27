package org.cometd4gwt.server;

import org.cometd.Client;

public interface ClientConnectionListener {
	void onConnect(Client client, String userId);
	void onDisconnect(Client client, String userId);
}