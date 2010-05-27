package org.cometd4gwt.client;

import com.google.gwt.core.client.JavaScriptObject;

class CometdJsni {

	native JavaScriptObject addSubscriber(String channel, JsoListener<? extends JavaScriptObject> listener)/*-{
		return $wnd.dojox.cometd.subscribe(channel, function(message){
			listener.@org.cometd4gwt.client.JsoListener::onMessageReceived(Lcom/google/gwt/core/client/JavaScriptObject;)(message);
		})
	}-*/;

	native void unsubscribe(JavaScriptObject subscription)/*-{
		$wnd.dojox.cometd.unsubscribe(subscription);
	}-*/;

	native void addListener(String channel, JsoListener<? extends JavaScriptObject> receiver)/*-{
		$wnd.dojox.cometd.addListener(channel, function(message){
			receiver.@org.cometd4gwt.client.JsoListener::onMessageReceived(Lcom/google/gwt/core/client/JavaScriptObject;)(message);
		})
	}-*/;

	native void publish(String channel, String serializedString)/*-{
		$wnd.dojox.cometd.publish(channel, { serializedString: ss });
	}-*/;

	native void connect(ConnectionConfig config, ScriptLoadListener loadListener)/*-{
		$wnd.dojo.require("dojox.cometd");
		var cometd = $wnd.dojox.cometd;

		$wnd.dojo.addOnLoad(function() {
			loadListener.@org.cometd4gwt.client.ScriptLoadListener::onLoad()();

//		    $wnd.dojo.addOnUnload(function() {
//				loadListener.@org.cometd4gwt.client.ScriptLoadListener::onUnload()();
//		    });

			// TODO - enable sending custom request header 
			// var name = config.@org.cometd4gwt.client.ConnectionConfig::requestHeaderName;
			var value = config.@org.cometd4gwt.client.ConnectionConfig::requestHeaderValue;
		    cometd.configure({
		        requestHeaders: {requestHeader:value},
		        url: config.@org.cometd4gwt.client.ConnectionConfig::url,
		        maxConnection: config.@org.cometd4gwt.client.ConnectionConfig::maxConnection,
		        maxNetworkDelay: config.@org.cometd4gwt.client.ConnectionConfig::maxNetworkDelay,
		        logLevel: 'debug'
		    });

		    cometd.handshake();
		});
	}-*/;

	native void disconnectAsync()/*-{
		$wnd.dojox.cometd.disconnect();
	}-*/;

	native void disconnectSync(String url)/*-{
		// code for IE7+, Firefox, Chrome, Opera, Safari
		if (window.XMLHttpRequest) {
			xmlhttp=new XMLHttpRequest();
		} 

		// code for IE6, IE5
		else {
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}

		xmlhttp.open("GET",url,false);
		xmlhttp.send(null);
	}-*/;
}