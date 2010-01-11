package org.cometd4gwt.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Tweet implements IsSerializable {
	private Date date;
	private String message;

	public Tweet() {
	}

	public Tweet(Date date, String message) {
		this.date = date;
		this.message = message;
	}

	@Override
	public String toString() {
		return "Tweet [date=" + date + ", message=" + message + "]";
	}
}