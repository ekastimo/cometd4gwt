package org.cometd4gwt.client;

import com.google.gwt.core.client.JavaScriptObject;

public class CometdJsni {

	native void addSubscriber(String channel, MessageListener<? extends JavaScriptObject> listener)/*-{
		var subscription = $wnd.dojox.cometd.subscribe(channel, function(message){
			listener.@org.cometd4gwt.client.MessageListener::onMessageReceived(Lcom/google/gwt/core/client/JavaScriptObject;)(message);
		})
	}-*/;

	native void addListener(String channel, MessageListener<? extends JavaScriptObject> receiver)/*-{
		$wnd.dojox.cometd.addListener(channel, function(message){
			receiver.@org.cometd4gwt.client.MessageListener::onMessageReceived(Lcom/google/gwt/core/client/JavaScriptObject;)(message);
		})
	}-*/;

	native void publish(String channel, String serializedString)/*-{
		$wnd.dojox.cometd.publish(channel, { serializedString: ss });
	}-*/;

	native void disconnect()/*-{
		$wnd.dojox.cometd.disconnect();
	}-*/;

	native void connect(ConnectionConfig config, ScriptLoadListener loadListener)/*-{
		$wnd.dojo.require("dojox.cometd");
		var cometd = $wnd.dojox.cometd;

		$wnd.dojo.addOnLoad(function() {
			loadListener.@org.cometd4gwt.client.ScriptLoadListener::onLoad()();

		    $wnd.dojo.addOnUnload(function() {
				loadListener.@org.cometd4gwt.client.ScriptLoadListener::onUnload()();
		    });

			// TODO - enable sending custom request header 
			// var name = config.@org.cometd4gwt.client.ConnectionConfig::requestHeaderName;
			var value = config.@org.cometd4gwt.client.ConnectionConfig::requestHeaderValue;
		    cometd.configure({
		        requestHeaders: {requestHeader:value},
		        url: config.@org.cometd4gwt.client.ConnectionConfig::url,
		        maxConnection: config.@org.cometd4gwt.client.ConnectionConfig::maxConnection,
		        logLevel: 'debug'
		    });

		    cometd.handshake();
		});
	}-*/;
}