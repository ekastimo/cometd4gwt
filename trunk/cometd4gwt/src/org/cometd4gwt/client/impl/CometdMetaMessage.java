package org.cometd4gwt.client.impl;

import com.google.gwt.core.client.JavaScriptObject;

public class CometdMetaMessage extends JavaScriptObject {
	protected CometdMetaMessage() {
	}

	public final int getId() {
		return Integer.parseInt(getIdAsString());
	}

	private final native String getIdAsString()/*-{
		return this.id;
	}-*/;

	public final native boolean isSuccessful()/*-{
		return this.successful;
	}-*/;

	public final native String getChannel() /*-{
		return this.channel;
	}-*/;

	public final native String getAction() /*-{
		return this.action;
	}-*/;
}