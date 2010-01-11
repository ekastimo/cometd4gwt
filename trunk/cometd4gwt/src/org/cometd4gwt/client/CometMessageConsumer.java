package org.cometd4gwt.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public interface CometMessageConsumer {
	void onMessageReceived(IsSerializable message);
}
