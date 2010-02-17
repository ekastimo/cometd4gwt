package org.cometd4gwt.server;

import java.util.HashMap;

import org.cometd4gwt.client.CometdConstants;

import com.google.gwt.user.client.rpc.IsSerializable;

public class JsonMap extends HashMap<String, Object> implements CometdConstants {
	public JsonMap(IsSerializable message) {
		put(SERIALIZED_STRING, Serializer.toString(message));
		put(OBJECT_MESSAGE, message);
	}
}
