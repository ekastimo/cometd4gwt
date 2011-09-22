package org.cometd4gwt.server;

import java.util.Date;

import javax.servlet.ServletException;

import org.cometd.Client;
import org.cometd4gwt.client.ChannelOf;
import org.cometd4gwt.client.CometMessageConsumer;
import org.cometd4gwt.client.Tweet;
import org.cometd4gwt.client.TwitterService;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class TwitterServiceImpl extends RemoteServiceServlet implements TwitterService {

	
	private CometdServer cometServer;
//	private String serverVertion = "" + new Date().getTime();
//
//	public Map<String, Object> getServerVertion() {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("serverVersion", serverVertion);
//		return map;
//	}
	

	@Override
	public void init() throws ServletException {
		cometServer = (CometdServer) getServletContext().getAttribute(CometdServer.ATTRIBUTE);
		cometServer.addClientConnectionListener(new ClientConnectionListener() {

			@Override
			public void onConnect(Client client, String request) {
//				client.deliver(client, SERVER_VERSION_CHANNEL, getServerVertion(), null);
				publishTweet(new Tweet(new Date(), toString(client, request) + " has came online"));
			}

			private String toString(Client client, String userId) {
				return "[client=" + client + ", userId=" + userId + "]";
			}

			@Override
			public void onDisconnect(Client client, String userId) {
				publishTweet(new Tweet(new Date(), client + " is ofline"));
			}
		});

		cometServer.subscribeChannel(ChannelOf.TWITTER, new CometMessageConsumer() {
			@Override
			public void onMessageReceived(IsSerializable message) {
				System.err.println("message on the server side: " + message);
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