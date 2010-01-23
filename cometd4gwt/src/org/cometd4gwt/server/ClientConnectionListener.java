package org.cometd4gwt.server;

import javax.servlet.http.HttpServletRequest;

import org.cometd.Client;

public interface ClientConnectionListener {
	void onConnect(Client client, HttpServletRequest request);
	void onDisconnect(Client client, HttpServletRequest request);
	void onDisconnect(Client client, String requestHeader);
}