package org.cometd4gwt.client;

import java.util.Date;

import org.cometd4gwt.client.impl.CometdClientImpl;
import org.cometd4gwt.client.impl.CometdMetaMessage;
import org.cometd4gwt.client.impl.Subscription;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Cometd4GwtTest implements EntryPoint, CometdConstants {
	final CometdClient cometdClient = new AutoReSubscribableClient(new CometdClientImpl(new CometdDojo()));
	private boolean subscribesAdded = false;
	private VerticalPanel verticalPanel = new VerticalPanel();

	private void log(String message) {
		verticalPanel.insert(new Label(message), 1);
	}

	public void onModuleLoad() {
		RootPanel.get().add(verticalPanel);
		verticalPanel.add(createTextArea());

		verticalPanel.add(new InlineAnchor("icon-cross", "Test Anchor", new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				log("CLICKED--------------------GWT.getVersion()=" + GWT.getVersion());
			}
		}));

		verticalPanel.add(new InlineAnchor("Test Anchor2", new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				log("CLICKED--------------------GWT.getVersion()=" + GWT.getVersion());
			}
		}));

		cometdClient.addConnectionListener(new CometConnectionListener() {
			@Override
			public void onConnected() {
				log("You are connected");
				addSubscribers();
			}

			@Override
			public void onDisconnected() {
				log("You are disconnected");
			}
		});

		cometdClient.addScriptLoadListener(new ScriptLoadListener() {
			@Override
			public void onLoad() {
				cometdClient.addListener("/meta/*", new JsoListener<CometdMetaMessage>() {
					@Override
					public void onMessageReceived(CometdMetaMessage metaMessage) {
						log("metaMessage=" + new JSONObject(metaMessage));
					}
				});

				cometdClient.addListener(SERVER_VERSION_CHANNEL, new ServerVersionSyncar());
			}
		});

		String cometdServletUrl = GWT.getHostPageBaseURL() + "cometd";
		log("Cometd servlet's URL - " + cometdServletUrl);
		ConnectionConfig connectionConfig = new ConnectionConfig(cometdServletUrl);
		connectionConfig.requestHeaderName = "userId";
		connectionConfig.requestHeaderValue = "" + ((int) (Math.random() * 1000));
		cometdClient.connect(connectionConfig);
	}

	private void addSubscribers() {
		if (!subscribesAdded) {
			subscribesAdded = true;

			final JavaScriptObject subscription = cometdClient.addSubscriber(ChannelOf.TWITTER, new CometMessageConsumer() {
				@Override
				public void onMessageReceived(IsSerializable message) {
					log("--" + message);
				}
			}, new JsoListener<Subscription>() {
				@Override
				public void onMessageReceived(Subscription subscription) {
					log(subscription._toString());
				}
			});

			log("subscription=" + new JSONObject(subscription));

//			new Timer() {
//				@Override
//				public void run() {
//					cometdClient.unsubscribe(subscription);
//					cometdClient.disconnect();
//				}
//			}.schedule(5000);
		}
	}

	private Widget createTextArea() {
		final TextArea textArea = new TextArea();
		textArea.setSize("400px", "50px");

		textArea.addKeyUpHandler(new KeyUpHandler() {
			TwitterServiceAsync twitterService = GWT.create(TwitterService.class);

			@Override
			public void onKeyUp(KeyUpEvent event) {
				int keyCode = event.getNativeKeyCode();
				if (keyCode == '\n' || keyCode == '\r') {
					Tweet tweet = new Tweet(new Date(), textArea.getText().trim());
					textArea.setText("");
					twitterService.publishTweet(tweet, new DefaultAsyncCallback<Void>());
				}
			}
		});

		return textArea;
	}
}