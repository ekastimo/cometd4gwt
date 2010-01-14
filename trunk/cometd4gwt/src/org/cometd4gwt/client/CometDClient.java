package org.cometd4gwt.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CometDClient {
	private List<CometConnectionListener> connectionListeners = new ArrayList<CometConnectionListener>();
	
	/**
	 * TODO - also add addListenner and check all the preconditions (is
	 * handshake called or is it a meta channel? etc) before calling as well as
	 * use idempotent methods concept.
	 * 
	 * also check - The wildcards can only be specified as last segment of the
	 * channel
	 */
	public native void addSubscriber(String channel, CometMessageConsumer consumer)/*-{
		var subscription =  $wnd.dojox.cometd.subscribe(channel, function(message){
			var isSerialable = @org.cometd4gwt.client.ClientSerializer::toObject(Ljava/lang/String;)(message.data.serializedString);
			consumer.@org.cometd4gwt.client.CometMessageConsumer::onMessageReceived(Lcom/google/gwt/user/client/rpc/IsSerializable;)(isSerialable);
		})
	}-*/;

	public native void addSubscribers(String[] channel, CometMessageConsumer[] consumer)/*-{
		var cometd = $wnd.dojox.cometd;
		$wnd.alert("length=");
		$wnd.alert("length=" + channel.length());
//		for(i = 0; i < )
//		cometd.subscribe(channel, function(message){
//			var isSerialable = @org.cometd4gwt.client.ClientSerializer::toObject(Ljava/lang/String;)(message.data.serializedString);
//			consumer.@org.cometd4gwt.client.CometMessageConsumer::onMessageReceived(Lcom/google/gwt/user/client/rpc/IsSerializable;)(isSerialable);
//		})
	}-*/;

	public native void addListener(String channel, CometMessageConsumer consumer)/*-{
		$wnd.dojox.cometd.addListener(channel, function(message){
			var isSerialable = @org.cometd4gwt.client.ClientSerializer::toObject(Ljava/lang/String;)(message.data.serializedString);
			consumer.@org.cometd4gwt.client.CometMessageConsumer::onMessageReceived(Lcom/google/gwt/user/client/rpc/IsSerializable;)(isSerialable);
		})
	}-*/;

	public native void publish(String channel, String message) /*-{
		$wnd.dojox.cometd.publish(channel, { name: message });
	}-*/;

	public native void publish(String channel, IsSerializable message)/*-{
		var ss = @org.cometd4gwt.client.ClientSerializer::toString(Lcom/google/gwt/user/client/rpc/IsSerializable;)(message);
		$wnd.dojox.cometd.publish(channel, { serializedString: ss });
	}-*/;

	public native void disconnect()/*-{
		$wnd.dojox.cometd.disconnect();
	}-*/;

	// TODO - enable sending custom request header 
	public native void connect(ConnectionConfig config)/*-{
		$wnd.dojo.require("dojox.cometd");
		var cometdJava = this;
		var cometd = $wnd.dojox.cometd;

		$wnd.dojo.addOnLoad(function() {
		    $wnd.dojo.addOnUnload(function() {
		        cometd.disconnect();
		    });

			// var name = config.@org.cometd4gwt.client.ConnectionConfig::requestHeaderName;
			var value = config.@org.cometd4gwt.client.ConnectionConfig::requestHeaderValue;
		    cometd.configure({
		        requestHeaders: {requestHeader:value},
		        url: config.@org.cometd4gwt.client.ConnectionConfig::url,
		        maxConnection: config.@org.cometd4gwt.client.ConnectionConfig::maxConnection,
		        logLevel: 'debug'
		    });

		    var _connected = false;
		    cometd.addListener('/meta/connect', function (message) {
		        var wasConnected = _connected;
		        _connected = message.successful === true;
		        if (!wasConnected && _connected) {
		            cometdJava.@org.cometd4gwt.client.CometDClient::onConnected()();
		        } else if (wasConnected && !_connected) {
		            cometdJava.@org.cometd4gwt.client.CometDClient::onDisconnected()();
		        }
		    });

		    cometd.handshake();
		});
	}-*/;

	// FIXME - not working :(
	public native void executeBatch(BatchExecution batchExecution) /*-{
		var cometd = $wnd.dojox.cometd;
		batchExecution.@org.cometd4gwt.client.BatchExecution::execute();
		
//		cometd.batch(function() {
//			batchExecution.@org.cometd4gwt.client.BatchExecution::execute();
//		});
		
//		cometd.startBatch();
//		batchExecution.@org.cometd4gwt.client.BatchExecution::execute();
//		cometd.endBatch();
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