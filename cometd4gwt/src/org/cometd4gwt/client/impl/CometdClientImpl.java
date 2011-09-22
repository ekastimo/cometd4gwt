package org.cometd4gwt.client.impl;

import java.util.ArrayList;
import java.util.List;

import org.cometd4gwt.client.CometConnectionListener;
import org.cometd4gwt.client.CometMessageConsumer;
import org.cometd4gwt.client.CometdClient;
import org.cometd4gwt.client.CometdConstants;
import org.cometd4gwt.client.CometdJavaScript;
import org.cometd4gwt.client.ConnectionConfig;
import org.cometd4gwt.client.JsoListener;
import org.cometd4gwt.client.MetaService;
import org.cometd4gwt.client.MetaServiceAsync;
import org.cometd4gwt.client.ScriptLoadListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CometdClientImpl implements CometdClient, CometdConstants {

	private List<ScriptLoadListener> scriptLoadListeners = new ArrayList<ScriptLoadListener>();
	private List<CometConnectionListener> connectionListeners = new ArrayList<CometConnectionListener>();
	private SubscriptionListeners subscriptionListeners = new SubscriptionListeners();
	private MetaServiceAsync metaService = GWT.create(MetaService.class);

	private String clientId;

	private boolean wasConnected = false;
	private final CometdJavaScript cometdJavaScript;

	public CometdClientImpl(final CometdJavaScript cometdJavaScript) {
		this.cometdJavaScript = cometdJavaScript;

		addScriptLoadListener(new ScriptLoadListener() {
			@Override
			public void onLoad() {
				addMetaListeners();
			}
		});

		Window.addCloseHandler(new CloseHandler<Window>() {
			@Override
			public void onClose(CloseEvent<Window> event) {
				cometdJavaScript.disconnectSync(GWT.getHostPageBaseURL() + "initializer?clientId=" + clientId);
			}
		});
	}

	@Override
	public void addListener(String channel, JsoListener<? extends JavaScriptObject> receiver) {
		if (channel.startsWith("/meta/") || channel.startsWith("/service/")) {
			cometdJavaScript.addListener(channel, receiver);
		} else {
			System.err.println(receiver + " can be added on to /meta/* and /service/* channels. Not " + channel);
		}
	}

	@Override
	public void connect(ConnectionConfig config) {
		cometdJavaScript.connect(config, new ScriptLoadListener() {
			@Override
			public void onLoad() {
				for (ScriptLoadListener l : scriptLoadListeners) {
					l.onLoad();
				}
			}
		});
	}

	@Override
	public JavaScriptObject addSubscriber(String channel, CometMessageConsumer consumer) {
		return addSubscriber(channel, consumer, null);
	}

	@Override
	public JavaScriptObject addSubscriber(String channel, final CometMessageConsumer consumer, JsoListener<Subscription> listener) {
		if (!isConnected()) {
			System.err.println("Subscriber (channel=" + channel + ", consumer=" + consumer + ") can't be added while not connected");
		} else if (channel.startsWith("/meta/")) {
			System.err.println(consumer + "cannot be added to any /meta/* channels " + channel);
		} else {
			subscriptionListeners.addDisposableListener(channel, listener);
			return cometdJavaScript.addSubscriber(channel, new JsoListener<GwtSerializedJso>() {
				@Override
				public void onMessageReceived(GwtSerializedJso javaScriptObject) {
					consumer.onMessageReceived(javaScriptObject.getObject());
				}
			});
		}

		return null;
	}

	@Override
	public void unsubscribe(JavaScriptObject subscription) {
		cometdJavaScript.unsubscribe(subscription);
	}

	@Override
	public void publish(String channelId, IsSerializable message, AsyncCallback<Void> callback) {
		metaService.publish(channelId, message, callback);
	}

	@Override
	public void publish(String channelId, IsSerializable message, String clientId, AsyncCallback<Void> callback) {
		metaService.publish(channelId, message, clientId, callback);
	}

	@Override
	public void disconnectAsync() {
		if (isConnected()) {
			cometdJavaScript.disconnectAsync();
		} else {
			System.err.println("not connected");
		}
	};

	@Override
	public final void addScriptLoadListener(ScriptLoadListener scriptLoadListener) {
		scriptLoadListeners.add(scriptLoadListener);
	}

	@Override
	public final void addConnectionListener(CometConnectionListener connectionListener) {
		connectionListeners.add(connectionListener);
	}

	@Override
	public final boolean isConnected() {
		return wasConnected;
	}

	private final void addMetaListeners() {

		/**
		 * Calls the fake serialization service to help the server to generate
		 * the serialization policy for once
		 */
		addListener(GENERATE_SERIALIZATION_POLICY_REQUEST, new JsoListener<CometdMetaMessage>() {
			@Override
			public void onMessageReceived(CometdMetaMessage javaScriptObject) {
				metaService.getSerializable(null, new AsyncCallback<IsSerializable>() {
					@Override
					public void onSuccess(IsSerializable result) {
					}

					@Override
					public void onFailure(Throwable caught) {
					}
				});
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

	@Override
	public void startBatch() {
		cometdJavaScript.startBatch();
	}

	@Override
	public void endBatch() {
		cometdJavaScript.endBatch();
	}
}