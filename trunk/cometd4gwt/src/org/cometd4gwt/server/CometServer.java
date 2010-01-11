package org.cometd4gwt.server;

import org.cometd.Bayeux;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CometServer {
	private final Bayeux bayeux;

	public CometServer(Bayeux bayeux) {
		this.bayeux = bayeux;
	}

	public void publishMessage(String channelId, IsSerializable message) {

	}
}