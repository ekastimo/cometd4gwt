package org.cometd4gwt.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Cometd provides two client side javascript libraries: Dojo and JQuery.
 * Implementation of this class wraps up the corresponding javascript calls
 */
public interface CometdJavaScript {

	JavaScriptObject addSubscriber(String channel, JsoListener<? extends JavaScriptObject> listener);

	void endBatch();

	void startBatch();

	void disconnectSync(String url);

	void disconnectAsync();

	void connect(ConnectionConfig config, ScriptLoadListener loadListener);

	void publish(String channel, String serializedString);

	void addListener(String channel, JsoListener<? extends JavaScriptObject> receiver);

	void unsubscribe(JavaScriptObject subscription);
}