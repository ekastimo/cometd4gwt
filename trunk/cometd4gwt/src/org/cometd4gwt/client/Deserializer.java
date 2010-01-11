package org.cometd4gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;

public class Deserializer {

	public static IsSerializable toObject(String serializedString) {
		SerializationStreamFactory ssf = GWT.create(SerializationService.class);
		
		try {
			return (IsSerializable) ssf.createStreamReader(serializedString).readObject();
		} catch (SerializationException e) {
			e.printStackTrace();
		}

		return null;
	}
}