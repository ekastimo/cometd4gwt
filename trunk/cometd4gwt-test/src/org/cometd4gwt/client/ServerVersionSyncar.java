package org.cometd4gwt.client;

import com.google.gwt.user.client.Window;

public class ServerVersionSyncar implements JsoListener<ServerVertion> {
	private ServerVertion serverVertion;

	@Override
	public void onMessageReceived(ServerVertion serverVertion) {
		if (this.serverVertion == null) {
			this.serverVertion = serverVertion;
		} else if (!this.serverVertion._equals(serverVertion)) {
			Window.Location.reload();
		}
	}
}