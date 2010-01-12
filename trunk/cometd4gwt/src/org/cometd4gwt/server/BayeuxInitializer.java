package org.cometd4gwt.server;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.cometd.Bayeux;

@SuppressWarnings("serial")
public class BayeuxInitializer extends GenericServlet {

	public void init() throws ServletException {
		Bayeux bayeux = (Bayeux) getServletContext().getAttribute(Bayeux.ATTRIBUTE);
		getServletContext().setAttribute(CometServer.ATTRIBUTE, new CometServer(bayeux));
	}

	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		throw new ServletException();
	}
}