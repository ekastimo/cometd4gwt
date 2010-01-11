package org.cometd4gwt.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.cometd.Bayeux;
import org.cometd.BayeuxListener;
import org.cometd.Channel;
import org.cometd.Client;
import org.cometd.Message;
import org.cometd.server.BayeuxService;
import org.cometd4gwt.client.Tweet;

public class HelloService extends BayeuxService {

	public HelloService(Bayeux bayeux) {
		super(bayeux, "hello");
		subscribe("/service/hello", "processHello");
	}

	public void processHello(Client remote, Message message) {
		Map<String, Object> input = (Map<String, Object>) message.getData();
		String name = (String) input.get("name");

		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("greeting", "Heyss, " + name);
		System.err.println("processHello");

		map.put("greeting", Serializer.toString(new Tweet(new Date(), "Cometd Rox!!!!")));
		remote.deliver(getClient(), "/hello", map, null);
	}
	
	public void createChannel() {
		Channel channel = getBayeux().getChannel("", true);
		getBayeux().addListener(new BayeuxListener() {
		});
	}
}