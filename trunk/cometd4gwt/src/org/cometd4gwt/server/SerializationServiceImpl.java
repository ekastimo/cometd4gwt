package org.cometd4gwt.server;

import org.cometd4gwt.client.SerializationService;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyProvider;

@SuppressWarnings("serial")
public class SerializationServiceImpl extends OpenRemoteServiceServlet implements SerializationService {

	@Override
	public SerializationPolicy getSerializationPolicy(String moduleBaseURL, String strongName) {
		final SerializationPolicy serializationPolicy = super.getSerializationPolicy(moduleBaseURL, strongName);

		System.err.println("getSerializationPolicy(" + moduleBaseURL + ", " + strongName + ")=" + serializationPolicy);

		ServerSerializer.setSerializationPolicy(serializationPolicy);
		ServerSerializer.setSerializationPolicyProvider(new SerializationPolicyProvider() {
			@Override
			public SerializationPolicy getSerializationPolicy(String moduleBaseURL, String serializationPolicyStrongName) {
				return serializationPolicy;
			}
		});

		return serializationPolicy;
	}

	/**
	 * The most complex method I ever saw :)
	 */
	@Override
	public IsSerializable getSerializable(IsSerializable isSerializable) {
		return isSerializable;
	}
}