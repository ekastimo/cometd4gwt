package org.cometd4gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;

class Deserializer {
	private static SerializationStreamFactory ssf = GWT.create(MetaService.class);

	public static IsSerializable toObject(String serializedString) {
		try {
			return (IsSerializable) ssf.createStreamReader(serializedString).readObject();
		} catch (Exception e) {
			Window.alert("DeserializeException=" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}
}