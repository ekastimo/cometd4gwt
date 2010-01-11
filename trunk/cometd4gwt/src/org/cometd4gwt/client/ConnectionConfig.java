package org.cometd4gwt.client;

public class ConnectionConfig {

	/**
	 * The URL of the Bayeux server this client will connect to
	 */
	public final String url;

	/**
	 * The log level. Possible values are: "warn", "info", "debug". Output to
	 * window.console if available
	 */
	public LogLevel logLevel = LogLevel.info;

	/**
	 * The max number of connections used to connect to the Bayeux server. Only
	 * change this value if you know exactly what is the client's connection
	 * limit and what "request queued behind long poll" means
	 */
	public int maxConnection = 2;

	/**
	 * The number of milliseconds of which the backoff time is incremented every
	 * time a connection with the Bayeux server fails. A reconnection will be
	 * attempted after the backoff time elapses
	 */
	public int backoffIncrement = 1000;

	/**
	 * The max number of milliseconds of the backoff time after which the
	 * backoff time is not incremented anymore
	 */
	public int maxBackoff = 60000;

	/**
	 * Controls whether the incoming extensions will be called in reverse order
	 * with respect to the registration order
	 */
	public boolean reverseIncomingExtensions = true;

	/**
	 * The max number of milliseconds to wait before considering a request to
	 * the Bayeux server failed.
	 */
	public int maxNetworkDelay = 10000;

	/**
	 * An object containing the request headers to be sent for every bayeux
	 * request (for example: {"My-Custom- Header":"MyValue"})
	 */
	public String requestHeaders = "{}";

	public ConnectionConfig(String url) {
		this.url = url;
	}
}