package org.cometd4gwt.client;

import java.util.ArrayList;
import java.util.List;

public class CometClient implements CometConstants {
	private List<CometConnectionListener> connectionListeners = new ArrayList<CometConnectionListener>();

	/**
	 * TODO - also add addListenner and check all the preconditions (is
	 * handshake called or is it a meta channel? etc) before calling as well as
	 * use idempotent methods concept.
	 * 
	 * also check - The wildcards can only be specified as last segment of the
	 * channel
	 */
	public native void subscribe(String channel, CometMessageConsumer consumer)/*-{
		$wnd.dojox.cometd.subscribe(channel, function(message){
			var isSerialable = @org.cometd4gwt.client.Deserializer::toObject(Ljava/lang/String;)(message.data.greeting);
			consumer.@org.cometd4gwt.client.CometMessageConsumer::onMessageReceived(Lcom/google/gwt/user/client/rpc/IsSerializable;)(isSerialable);
		})
	}-*/;

	public native void publish(String channel, String message) /*-{
		$wnd.dojox.cometd.publish(channel, { name: message });
	}-*/;

	public native void disconnect()/*-{
		$wnd.dojox.cometd.disconnect();
	}-*/;

	public native void connect(ConnectionConfig config)/*-{
		$wnd.dojo.require("dojox.cometd");
		var cometdJava = this;
		var cometd = $wnd.dojox.cometd;

		$wnd.dojo.addOnLoad(function() {
		    $wnd.dojo.addOnUnload(function() {
		        cometd.disconnect();
		    });

		    cometd.configure({
		        url: config.@org.cometd4gwt.client.ConnectionConfig::url,
		        logLevel: 'debug'
		    });

		    var _connected = false;
		    cometd.addListener('/meta/connect', function (message) {
		        var wasConnected = _connected;
		        _connected = message.successful === true;
		        if (!wasConnected && _connected) {
		            cometdJava.@org.cometd4gwt.client.CometClient::onConnected()();
		        } else if (wasConnected && !_connected) {
		            cometdJava.@org.cometd4gwt.client.CometClient::onDisconnected()();
		        }
		    });

		    cometd.handshake();
		});
	}-*/;

	// FIXME - not working :(
	public native void executeBatch(BatchExecution batchExecution) /*-{
		$wnd.dojox.cometd.batch(function() {
			batchExecution.@org.cometd4gwt.client.BatchExecution::execute();
		});
	}-*/;

	public void addConnectionListener(CometConnectionListener connectionListener) {
		connectionListeners.add(connectionListener);
	}

	public void onConnected() {
		for (CometConnectionListener listener : connectionListeners) {
			listener.onConnected();
		}
	}

	public void onDisconnected() {
		for (CometConnectionListener listener : connectionListeners) {
			listener.onDisconnected();
		}
	}
}