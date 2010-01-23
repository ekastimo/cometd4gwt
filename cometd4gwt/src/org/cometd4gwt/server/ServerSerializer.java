package org.cometd4gwt.server;

import java.lang.reflect.Method;

import org.cometd4gwt.client.MetaService;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyProvider;

public class ServerSerializer {

	private static SerializationPolicy serializationPolicy = null;

	public static String toString(IsSerializable object) {
		try {
			Method serviceMethod = MetaService.class.getMethod("getSerializable", IsSerializable.class);
			return RPC.encodeResponseForSuccess(serviceMethod, object, serializationPolicy);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SerializationException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void setSerializationPolicy(SerializationPolicy serializationPolicy) {
		ServerSerializer.serializationPolicy = serializationPolicy;
	}

	public static boolean isSerializationPolicyNull() {
		return serializationPolicy == null;
	}
}