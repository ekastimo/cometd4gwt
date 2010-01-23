package org.cometd4gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

public interface MetaServiceAsync {
	void getSerializable(IsSerializable isSerializable, AsyncCallback<IsSerializable> callback);
}