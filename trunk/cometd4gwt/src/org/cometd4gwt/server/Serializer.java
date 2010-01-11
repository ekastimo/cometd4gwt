package org.cometd4gwt.server;

import java.lang.reflect.Method;

import org.cometd4gwt.client.SerializationService;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;

public class Serializer {

	public static String toString(IsSerializable object) {
		try {
			Method serviceMethod = SerializationService.class.getMethod("getSerializable");
			return RPC.encodeResponseForSuccess(serviceMethod, object);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SerializationException e) {
			e.printStackTrace();
		}

		return null;
	}
}