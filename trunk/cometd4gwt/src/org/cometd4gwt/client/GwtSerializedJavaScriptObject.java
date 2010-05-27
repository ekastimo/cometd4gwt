package org.cometd4gwt.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtSerializedJavaScriptObject extends JavaScriptObject {
	protected GwtSerializedJavaScriptObject() {
	}
	
	public final IsSerializable getObject() {
		return Deserializer.toObject(getSerializedString());
	}

	private final native String getSerializedString()/*-{
		return this.data.serializedString;
	}-*/;
}
