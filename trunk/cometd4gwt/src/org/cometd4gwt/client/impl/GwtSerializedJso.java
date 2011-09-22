package org.cometd4gwt.client.impl;

import org.cometd4gwt.client.MetaService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;

class GwtSerializedJso extends JavaScriptObject {
	private static SerializationStreamFactory ssf = GWT.create(MetaService.class);

	protected GwtSerializedJso() {
	}

	public final IsSerializable getObject() {
		try {
			return (IsSerializable) ssf.createStreamReader(getSerializedString()).readObject();
		} catch (Exception e) {
			Window.alert("DeserializeException=" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	private final native String getSerializedString()/*-{
		return this.data.serializedString;
	}-*/;
}