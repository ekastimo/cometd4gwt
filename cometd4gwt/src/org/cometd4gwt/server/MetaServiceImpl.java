package org.cometd4gwt.server;

import javax.servlet.ServletException;

import org.cometd4gwt.client.MetaService;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.server.rpc.SerializationPolicy;

@SuppressWarnings("serial")
public class MetaServiceImpl extends OpenRemoteServiceServlet implements MetaService {

	private CometdServer cometServer;

	@Override
	public void init() throws ServletException {
		cometServer = (CometdServer) getServletContext().getAttribute(CometdServer.ATTRIBUTE);
	}

	@Override
	public SerializationPolicy getSerializationPolicy(String moduleBaseURL, String strongName) {
		SerializationPolicy serializationPolicy = super.getSerializationPolicy(moduleBaseURL, strongName);

		if (Serializer.isSerializationPolicyNull()) {
			Serializer.setSerializationPolicy(serializationPolicy);
		}

		return serializationPolicy;
	}

	@Override
	public IsSerializable getSerializable(IsSerializable isSerializable) {
		return isSerializable;
	}

	@Override
	public void publish(String channelId, IsSerializable message) {
		cometServer.publish(channelId, message);
	}

	@Override
	public void publish(String channelId, IsSerializable message, String clientId) {
		cometServer.publish(channelId, message, clientId);
	}
}