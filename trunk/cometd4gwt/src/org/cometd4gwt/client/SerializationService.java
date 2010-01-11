package org.cometd4gwt.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("serializationService")
public interface SerializationService extends RemoteService {
	IsSerializable getSerializable();
}