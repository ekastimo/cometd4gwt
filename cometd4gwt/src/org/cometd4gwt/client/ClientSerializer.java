package org.cometd4gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

public class ClientSerializer {
	private static SerializationStreamFactory ssf = GWT.create(SerializationService.class);

	public static IsSerializable toObject(String serializedString) {
		try {
			return (IsSerializable) ssf.createStreamReader(serializedString).readObject();
		} catch (Exception e) {
			Window.alert("DeserializeException=" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	public static String toString(IsSerializable object) {
		SerializationStreamWriter streamWriter = ssf.createStreamWriter();
		try {
			streamWriter.writeObject(object);
			return streamWriter.toString();
		} catch (SerializationException e) {
			e.printStackTrace();
		}

		return null;
	}
}