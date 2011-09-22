package org.cometd4gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class DefaultAsyncCallback<T> implements AsyncCallback<T> {

	@Override
	public void onFailure(Throwable caught) {
	}

	@Override
	public void onSuccess(T result) {
	}
}
