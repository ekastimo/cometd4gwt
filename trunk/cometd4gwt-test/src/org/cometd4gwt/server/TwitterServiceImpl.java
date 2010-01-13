package org.cometd4gwt.server;

import java.util.Date;

import javax.servlet.ServletException;

import org.cometd4gwt.client.ChannelOf;
import org.cometd4gwt.client.Tweet;
import org.cometd4gwt.client.TwitterService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class TwitterServiceImpl extends RemoteServiceServlet implements TwitterService {

	private CometDServer cometServer;

	@Override
	public void init() throws ServletException {
		cometServer = (CometDServer) getServletContext().getAttribute(CometDServer.ATTRIBUTE);
		cometServer.addClientConnectionListener(new ClientConnectionListener() {

			@Override
			public void onConnect(String clientId) {
				publishTweet(new Tweet(new Date(), "[clientId=" + clientId + "] has came online"));
			}

			@Override
			public void onDisconnect(String clientId) {
				publishTweet(new Tweet(new Date(), "[clientId=" + clientId + "] is ofline"));
			}
		});
	}

	@Override
	public void publishTweet(Tweet tweet) {
		cometServer.publish(ChannelOf.TWITTER, tweet);
	}
}