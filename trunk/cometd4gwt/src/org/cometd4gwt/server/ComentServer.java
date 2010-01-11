package org.cometd4gwt.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cometd.Bayeux;
import org.cometd.BayeuxListener;
import org.cometd.Channel;
import org.cometd.Client;
import org.cometd.Message;
import org.cometd.server.BayeuxService;
import org.cometd4gwt.client.Tweet;

public class ComentServer extends BayeuxService {
	private List<ClientConnectionListener> ccls = new ArrayList<ClientConnectionListener>();

	public ComentServer(Bayeux bayeux) {
		super(bayeux, "hello");
		subscribe("/service/hello", "processHello");

		subscribe("/meta/connect", "onConnect");
		subscribe("/meta/disconnect", "onDisconnect");
	}

	public void onConnect(Client remote, Message message) {

		// FIXME - be sure that when a client connects for the first time, the message id is 2
		if (message.getId().equals("2")) {
			for (ClientConnectionListener l : ccls) {
				l.onConnect(message.getClientId());
			}
		}
	}

	public void onDisconnect(Client remote, Message message) {
		for (ClientConnectionListener l : ccls) {
			l.onDisconnect(message.getClientId());
		}
	}

	public void addClientConnectionListener(ClientConnectionListener listener) {
		ccls.add(listener);
	}

	public void processHello(Client remote, Message message) {
		Map<String, Object> input = (Map<String, Object>) message.getData();
		String name = (String) input.get("name");

		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("greeting", "Heyss, " + name);

		map.put("greeting", Serializer.toString(new Tweet(new Date(), "Cometd Rox!!!!")));
		remote.deliver(getClient(), "/hello", map, null);
	}

	public void createChannel() {
		Channel channel = getBayeux().getChannel("", true);

		getBayeux().addListener(new BayeuxListener() {
		});
	}
}