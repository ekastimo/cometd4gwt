package org.cometd4gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TwitterServiceAsync {
	void publishTweet(Tweet tweet, AsyncCallback<Void> callback);
}