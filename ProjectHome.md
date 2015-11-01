Cometd4Gwt makes use of GWT's JSNI to wrap up the existing CometD project's JavaScript client and utilizes the GWT's existing Serialization framework to transfer the object (that implements IsSerializable) from the server to clients.

Source code includes a sample project along with eclipse project file for Google Eclipse Plugin.

Here's how the client code looks like,

```
final CometDClient client = new AutoReSubscribableClient(new CometdClientImpl(new CometdDojo()));

client.addConnectionListener(new CometConnectionListener() {

	@Override
	public void onConnected() {

		// Register to receive object message
		client.addSubscriber("/twitter", new CometMessageConsumer() {
			@Override
			public void onMessageReceived(IsSerializable message) {
				// Process object received from the server
			}
		});
	}

	@Override
	public void onDisconnected() {
	}
});

String url = "http://" + Window.Location.getHost() + "/cometd";
client.connect(new ConnectionConfig(url));
```

If you have any question, please drop a message at http://groups.google.com/group/cometd4gwt