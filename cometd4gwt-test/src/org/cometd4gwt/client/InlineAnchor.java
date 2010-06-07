package org.cometd4gwt.client;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;

public class InlineAnchor extends Anchor {
	public InlineAnchor(String text, ClickHandler handler) {
		super(text, true);
		// setText(text);
		addClickHandler(handler);
		// addStyleName("link-button");
	}

	public InlineAnchor(String iconStyle, String text, ClickHandler handler) {
		this(text, handler);
		addStyleName(iconStyle);

		// addStyleName(isIE() ? "padding-left-ie" : "padding-left");
	}

	private boolean isIE() {
		return getUserAgent().contains("msie");
	}

	private static native String getUserAgent() /*-{
		return navigator.userAgent.toLowerCase();
	}-*/;
}
