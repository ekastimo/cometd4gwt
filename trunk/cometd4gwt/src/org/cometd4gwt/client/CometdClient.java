package org.cometd4gwt.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

public interface CometdClient {
	void addConnectionListener(CometConnectionListener connectionListener);

	boolean isConnected();

	void addListener(String channel, JsoListener<? extends JavaScriptObject> receiver);

	void connect(ConnectionConfig config);

	JavaScriptObject addSubscriber(String channel, CometMessageConsumer consumer);

	JavaScriptObject addSubscriber(String channel, CometMessageConsumer consumer, JsoListener<Subscription> listener);

	void unsubscribe(JavaScriptObject subscription);

	void publish(String channelId, IsSerializable message, AsyncCallback<Void> callback);

	void publish(String channelId, IsSerializable message, String clientId, AsyncCallback<Void> callback);

	void disconnectAsync();

	void addScriptLoadListener(ScriptLoadListener scriptLoadListener);
}
