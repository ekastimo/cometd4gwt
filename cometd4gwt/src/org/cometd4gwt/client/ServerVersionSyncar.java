package org.cometd4gwt.client;

import com.google.gwt.user.client.Window;

public class ServerVersionSyncar implements JsoListener<ServerVersion> {
	private ServerVersion serverVertion;

	@Override
	public void onMessageReceived(ServerVersion serverVertion) {
		if (this.serverVertion == null) {
			this.serverVertion = serverVertion;
		} else if (!this.serverVertion._equals(serverVertion)) {
			Window.alert("Server has just restarted. Click OK to reload the client.");
			Window.Location.reload();
		}
	}
}