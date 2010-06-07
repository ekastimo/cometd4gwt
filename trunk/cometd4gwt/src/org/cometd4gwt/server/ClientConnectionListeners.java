package org.cometd4gwt.server;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.cometd.Client;

@SuppressWarnings("serial")
public class ClientConnectionListeners extends ArrayList<ClientConnectionListener> implements ClientConnectionListener {

	private final ExecutorService executorService;
	private final ClientConnectionListeners _this;

	public ClientConnectionListeners() {
		this._this = this;
		this.executorService = new ThreadPoolExecutor(10, 100, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
	}

	@Override
	public void onConnect(final Client client, final String userId) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				for (ClientConnectionListener ccl : _this) {
					ccl.onConnect(client, userId);
				}
			}
		});
	}

	@Override
	public void onDisconnect(final Client client, final String userId) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				for (ClientConnectionListener ccl : _this) {
					ccl.onDisconnect(client, userId);
				}
			}
		});
	}
}
