package org.cometd4gwt.client;

import com.google.gwt.core.client.JavaScriptObject;

public class ServerVertion extends JavaScriptObject {
	protected ServerVertion() {
	}

	private final native String getServerVertion() /*-{
		return this.serverVertion;
	}-*/;

	public final boolean _equals(Object obj) {
		if (obj instanceof ServerVertion) {
			return ((ServerVertion) obj).getServerVertion().equals(this.getServerVertion());
		}

		return false;
	}
}