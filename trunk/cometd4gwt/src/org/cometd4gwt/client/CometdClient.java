package org.cometd4gwt.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CometdClient implements CometdConstants {

	private List<ScriptLoadListener> scriptLoadListeners = new ArrayList<ScriptLoadListener>();
	private List<CometConnectionListener> connectionListeners = new ArrayList<CometConnectionListener>();
	private SubscriptionListeners subscriptionListeners = new SubscriptionListeners();
	private MetaServiceAsync metaService = GWT.create(MetaService.class);

	private String clientId;

	private boolean wasConnected = false;
	private CometdJsni cometd = new CometdJsni();

	public CometdClient() {
		addScriptLoadListener(new ScriptLoadListener() {
			@Override
			public void onLoad() {
				addMetaListeners();
			}

			@Override
			public void onUnload() {
				cometd.disconnectSync(GWT.getHostPageBaseURL() + "initializer?clientId=" + clientId);
			}
		});
	}

	private final void addMetaListeners() {

		/**
		 * Calls the fake serialization service to help the server to generate
		 * the serialization policy for once
		 */
		addListener(GENERATE_SERIALIZATION_POLICY_REQUEST, new JsoListener<CometdJso>() {
			@Override
			public void onMessageReceived(CometdJso javaScriptObject) {
				metaService.getSerializable(null, new DefaultAsyncCallback<IsSerializable>());
			}
		});

		/**
		 * Subscription complete listeners
		 */
		addListener("/meta/subscribe", subscriptionListeners);

		/**
		 * Client id collector. ClientId is required to call the Sync disconnect
		 * method. Cometd JavaScript client doesn't provide any Sync disconnect
		 * method.
		 */
		addListener("/meta/handshake", new JsoListener<Handshake>() {
			@Override
			public void onMessageReceived(Handshake handshake) {
				clientId = handshake.getClientId();
			}
		});

		/**
		 * The most obvious connection listener
		 */
		addListener("/meta/connect", new JsoListener<CometdConnectionMessage>() {
			@Override
			public void onMessageReceived(CometdConnectionMessage connectMessage) {
				if (!wasConnected && connectMessage.isConnected()) {
					wasConnected = true;
					for (CometConnectionListener listener : connectionListeners) {
						listener.onConnected();
					}
				} else if (wasConnected && !connectMessage.isConnected()) {
					wasConnected = false;
					for (CometConnectionListener listener : connectionListeners) {
						listener.onDisconnected();
					}
				}
			}
		});

		/**
		 * To get explicit disconnect message
		 */
		addListener("/meta/disconnect", new JsoListener<CometdConnectionMessage>() {
			@Override
			public void onMessageReceived(CometdConnectionMessage connectMessage) {
				wasConnected = false;
				for (CometConnectionListener listener : connectionListeners) {
					listener.onDisconnected();
				}
			}
		});
	}

	public void addListener(String channel, JsoListener<? extends JavaScriptObject> receiver) {
		if (channel.startsWith("/meta/") || channel.startsWith("/service/")) {
			cometd.addListener(channel, receiver);
		} else {
			System.err.println(receiver + " can be added on to /meta/* and /service/* channels. Not " + channel);
		}
	}

	public void connect(ConnectionConfig config) {
		cometd.connect(config, new ScriptLoadListener() {

			@Override
			public void onLoad() {
				for (ScriptLoadListener l : scriptLoadListeners) {
					l.onLoad();
				}
			}

			@Override
			public void onUnload() {
				for (ScriptLoadListener l : scriptLoadListeners) {
					l.onUnload();
				}
			}
		});
	}

	public JavaScriptObject addSubscriber(String channel, CometMessageConsumer consumer) {
		return addSubscriber(channel, consumer, null);
	}

	public JavaScriptObject addSubscriber(String channel, final CometMessageConsumer consumer,
			JsoListener<Subscription> listener) {
		if (!isConnected()) {
			System.err.println("Subscriber (channel=" + channel + ", consumer=" + consumer
					+ ") can't be added while not connected");
		} else if (channel.startsWith("/meta/")) {
			System.err.println(consumer + "cannot be added to any /meta/* channels " + channel);
		} else {
			subscriptionListeners.addDisposableListener(channel, listener);
			return cometd.addSubscriber(channel, new JsoListener<GwtSerializedJavaScriptObject>() {
				@Override
				public void onMessageReceived(GwtSerializedJavaScriptObject javaScriptObject) {
					consumer.onMessageReceived(javaScriptObject.getObject());
				}
			});
		}

		return null;
	}

	public void unsubscribe(JavaScriptObject subscription) {
		cometd.unsubscribe(subscription);
	}

	public void publish(String channelId, IsSerializable message, AsyncCallback<Void> callback) {
		metaService.publish(channelId, message, callback);
	}

	public void publish(String channelId, IsSerializable message, String clientId, AsyncCallback<Void> callback) {
		metaService.publish(channelId, message, clientId, callback);
	}

	public void disconnectAsync() {
		if (isConnected()) {
			cometd.disconnectAsync();
		} else {
			System.err.println("not connected");
		}
	};

	public final void addScriptLoadListener(ScriptLoadListener scriptLoadListener) {
		scriptLoadListeners.add(scriptLoadListener);
	}

	public final void addConnectionListener(CometConnectionListener connectionListener) {
		connectionListeners.add(connectionListener);
	}

	public final boolean isConnected() {
		return wasConnected;
	}
}