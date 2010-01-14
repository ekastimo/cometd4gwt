package org.cometd4gwt.server;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.cometd.Client;
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
			public void onConnect(Client client, HttpServletRequest request) {
				publishTweet(new Tweet(new Date(), toString(client, request) + " has came online"));
			}

			@Override
			public void onDisconnect(Client client, HttpServletRequest request) {
				publishTweet(new Tweet(new Date(), toString(client, request) + " is ofline"));
			}

			private String toString(Client client, HttpServletRequest request) {
				if (request == null) {
					return "[client=" + client + ", request=null]";
				} else {
					return "[client=" + client + ", requestHeader=" + request.getHeader("requestHeader") + "]";
				}
			}
		});
	}
	@Override
	public void publishTweet(Tweet tweet) {
		cometServer.publish(ChannelOf.TWITTER, tweet);
		cometServer.publish(ChannelOf.TWITTER2, tweet);
	}
	
	public static void main(String[] args) {
		System.err.println(args.getClass());
	}
}