package org.cometd4gwt.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class Cometd4GwtTest implements EntryPoint, CometConstants {
	final CometDClient cometClient = new CometDClient();

	public void onModuleLoad() {
		RootPanel.get().add(createTextArea());

		cometClient.addConnectionListener(new CometConnectionListener() {
			@Override
			public void onConnected() {
				log("You are connected");

				cometClient.addSubscriber(ChannelOf.TWITTER, new CometMessageConsumer() {
					@Override
					public void onMessageReceived(IsSerializable message) {
						log("1-" + message);
					}
				});

//				cometClient.addSubscriber(ChannelOf.TWITTER2, new CometMessageConsumer() {
//					@Override
//					public void onMessageReceived(IsSerializable message) {
//						log("2-" + message);
//					}
//				});

//				cometClient.executeBatch(new BatchExecution() {
//					@Override
//					public void execute() {
//						System.err.println("executing batch");
//
//						cometClient.addSubscriber(ChannelOf.TWITTER2, new CometMessageConsumer() {
//							@Override
//							public void onMessageReceived(IsSerializable message) {
//								log("2-" + message);
//							}
//						});
//					}
//				});

//				cometClient.addSubscribers(new String[] { ChannelOf.TWITTER, ChannelOf.TWITTER2 }, null);
			}

			@Override
			public void onDisconnected() {
				log("You are disconnected");
			}
		});

		ConnectionConfig connectionConfig = new ConnectionConfig("http://" + Window.Location.getHost() + "/cometd");
		connectionConfig.requestHeaderName = "userId";
		connectionConfig.requestHeaderValue = "123";
		cometClient.connect(connectionConfig);
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

	private static void log(String message) {
		RootPanel.get().add(new Label(message));
	}
}