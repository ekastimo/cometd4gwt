package org.cometd4gwt.client;

import com.google.gwt.core.client.JavaScriptObject;

public class CometdMessage extends JavaScriptObject {
	protected CometdMessage() {
	}

	public final int getId() {
		return Integer.parseInt(_getId());
	}
	
	private final native String _getId()/*-{
		return this.id;
	}-*/;

	public final native boolean isSuccessful()/*-{
		return this.successful;
	}-*/;

	public final native String getChannel() /*-{
		return this.channel;
	}-*/;
}