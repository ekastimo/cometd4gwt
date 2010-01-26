package org.cometd4gwt.client;

import com.google.gwt.core.client.JavaScriptObject;

public class CometdJso extends JavaScriptObject {
	protected CometdJso() {
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