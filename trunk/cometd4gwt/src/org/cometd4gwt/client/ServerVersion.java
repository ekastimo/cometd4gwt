package org.cometd4gwt.client;

import com.google.gwt.core.client.JavaScriptObject;

public class ServerVersion extends JavaScriptObject {
	protected ServerVersion() {
	}

	public final native String getVersion() /*-{
		return this.data.version;
	}-*/;

	public final boolean _equals(Object obj) {
		if (obj instanceof ServerVersion) {
			return ((ServerVersion) obj).getVersion().equals(this.getVersion());
		}

		return false;
	}
}