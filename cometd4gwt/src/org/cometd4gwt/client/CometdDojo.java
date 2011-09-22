package org.cometd4gwt.client;

import com.google.gwt.core.client.JavaScriptObject;

public class CometdDojo implements CometdJavaScript {

	@Override
	public native JavaScriptObject addSubscriber(String channel, JsoListener<? extends JavaScriptObject> listener)/*-{
		return $wnd.dojox.cometd
				.subscribe(
						channel,
						function(message) {
							listener.@org.cometd4gwt.client.JsoListener::onMessageReceived(Lcom/google/gwt/core/client/JavaScriptObject;)(message);
						})
	}-*/;

	@Override
	public native void unsubscribe(JavaScriptObject subscription)/*-{
		$wnd.dojox.cometd.unsubscribe(subscription);
	}-*/;

	@Override
	public native void addListener(String channel, JsoListener<? extends JavaScriptObject> receiver)/*-{
		$wnd.dojox.cometd
				.addListener(
						channel,
						function(message) {
							receiver.@org.cometd4gwt.client.JsoListener::onMessageReceived(Lcom/google/gwt/core/client/JavaScriptObject;)(message);
						})
	}-*/;

	@Override
	public native void publish(String channel, String serializedString)/*-{
		$wnd.dojox.cometd.publish(channel, {
			serializedString : ss
		});
	}-*/;

	@Override
	public native void connect(ConnectionConfig config, ScriptLoadListener loadListener)/*-{
		$wnd.dojo.require("dojox.cometd");
		var cometd = $wnd.dojox.cometd;

		$wnd.dojo
				.addOnLoad(function() {
					loadListener.@org.cometd4gwt.client.ScriptLoadListener::onLoad()();

					//					$wnd.dojo.addOnUnload(function() {
					//							loadListener.@org.cometd4gwt.client.ScriptLoadListener::onUnload()();
					//					});

					// TODO - enable sending custom request header 
					// var name = config.@org.cometd4gwt.client.ConnectionConfig::requestHeaderName;
					var value = config.@org.cometd4gwt.client.ConnectionConfig::requestHeaderValue;
					cometd
							.configure({
								autoBatch : true,
								requestHeaders : {
									requestHeader : value
								},
								url : config.@org.cometd4gwt.client.ConnectionConfig::url,
								maxConnection : config.@org.cometd4gwt.client.ConnectionConfig::maxConnection,
								maxNetworkDelay : config.@org.cometd4gwt.client.ConnectionConfig::maxNetworkDelay,
								logLevel : 'debug'
							});

					cometd.handshake();
				});
	}-*/;

	@Override
	public native void disconnectAsync()/*-{
		$wnd.dojox.cometd.disconnect();
	}-*/;

	@Override
	public native void disconnectSync(String url)/*-{
		// code for IE7+, Firefox, Chrome, Opera, Safari
		if (window.XMLHttpRequest) {
			xmlhttp = new XMLHttpRequest();
		}

		// code for IE6, IE5
		else {
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}

		xmlhttp.open("GET", url, false);
		xmlhttp.send(null);
	}-*/;

	@Override
	public native void startBatch() /*-{
		$wnd.dojox.cometd.startBatch();
	}-*/;

	@Override
	public native void endBatch()/*-{
		$wnd.dojox.cometd.endBatch();
	}-*/;
}