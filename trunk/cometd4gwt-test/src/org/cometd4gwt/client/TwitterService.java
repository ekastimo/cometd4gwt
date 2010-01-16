package org.cometd4gwt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("twitter")
public interface TwitterService extends RemoteService {
	void publishTweet(Tweet tweet);
}
