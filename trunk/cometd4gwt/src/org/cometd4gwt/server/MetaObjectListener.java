package org.cometd4gwt.server;

import com.google.gwt.user.client.rpc.IsSerializable;

public interface MetaObjectListener {

	void onMetaObjectReceived(String id, IsSerializable object);

}
