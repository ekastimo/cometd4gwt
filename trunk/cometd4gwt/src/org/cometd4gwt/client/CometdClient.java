package org.cometd4gwt.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CometdClient implements CometdConstants {

	private List<ScriptLoadListener> scriptLoadListeners = new ArrayList<ScriptLoadListener>();
	private List<CometConnectionListener> connectionListeners = new ArrayList<CometConnectionListener>();
	private SubscriptionListener subscriptionListeners = new SubscriptionListener();

	private boolean isScriptLoaded = false;
	private boolean wasConnected = false;
	private CometdJsni cometd = new CometdJsni();

	public CometdClient() {
		addScriptLoadListener(new ScriptLoadListener() {
			@Override
			public void onLoad() {
				addSubscriptionListeners();
			}

			@Override
			public void onUnload() {
				disconnect();
			}
		});

//		cometd.init();
//		cometd.addScriptLoadListener(new ScriptLoadListener() {
//
//			@Override
//			public void onLoad() {
//				for (ScriptLoadListener l : scriptLoadListeners) {
//					l.onLoad();
//				}
//			}
//
//			@Override
//			public void onUnload() {
//				for (ScriptLoadListener l : scriptLoadListeners) {
//					l.onUnload();
//				}
//			}
//		});
	}

	public void addSubscriber(String channel, CometMessageConsumer consumer) {
		addSubscriber(channel, consumer, null);
	}

	public void addSubscriber(String channel, final CometMessageConsumer consumer, JsoListener<Subscription> listener) {
		if (!isConnected()) {
			System.err.println("Subscriber (channel=" + channel + ", consumer=" + consumer
					+ ") can't be added while not connected");
		} else if (channel.startsWith("/meta/")) {
			System.err.println(consumer + "cannot be added to any /meta/* channels " + channel);
		} else {
			subscriptionListeners.addDisposableListener(channel, listener);
			cometd.addSubscriber(channel, new JsoListener<GwtSerializedJavaScriptObject>() {
				@Override
				public void onMessageReceived(GwtSerializedJavaScriptObject javaScriptObject) {
					consumer.onMessageReceived(javaScriptObject.getObject());
				}
			});
		}
	}

	public void addListener(String channel, JsoListener<? extends JavaScriptObject> receiver) {
		if (channel.startsWith("/meta/") || channel.startsWith("/service/")) {
			cometd.addListener(channel, receiver);
		} else {
			System.err.println(receiver + " can be added on to /meta/* and /service/* channels. Not " + channel);
		}
	}

	public void publish(String channel, IsSerializable message) {
		String serializedString = ClientSerializer.toString(message);
		cometd.publish(channel, serializedString);
	};

	public void disconnect() {
		if (isConnected()) {
			cometd.disconnect();
		} else {
			System.err.println("not connected");
		}
	};

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

//		cometd.connect(config);
	}

	public void addSubscriptionListeners() {
		addListener(GENERATE_SERIALIZATION_POLICY_REQUEST, new JsoListener<CometdMessage>() {
			@Override
			public void onMessageReceived(CometdMessage javaScriptObject) {
				SerializationServiceAsync ss = GWT.create(SerializationService.class);
				ss.getSerializable(null, new DefaultAsyncCallback<IsSerializable>());
			}
		});

		addListener("/meta/subscribe", subscriptionListeners);

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
					onDisconnected();
				}
			}
		});

		addListener("/meta/disconnect", new JsoListener<CometdConnectionMessage>() {
			@Override
			public void onMessageReceived(CometdConnectionMessage connectMessage) {
				System.err.println("DISconnect: " + new JSONObject(connectMessage));
				wasConnected = false;
				onDisconnected();
			}
		});
	}

	public void addScriptLoadListener(ScriptLoadListener scriptLoadListener) {
		scriptLoadListeners.add(scriptLoadListener);
	}

	public void addConnectionListener(CometConnectionListener connectionListener) {
		connectionListeners.add(connectionListener);
	}

	public void onDisconnected() {
		for (CometConnectionListener listener : connectionListeners) {
			listener.onDisconnected();
		}
	}

	public final boolean isConnected() {
		return wasConnected;
	}
}