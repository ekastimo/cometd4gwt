package org.cometd4gwt.client;

import com.google.gwt.core.client.JavaScriptObject;

public interface MessageListener<M extends JavaScriptObject> {
	public void onMessageReceived(M javaScriptObject);
}
