package org.cometd4gwt.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cometd.Bayeux;

@SuppressWarnings("serial")
public class BayeuxInitializer extends HttpServlet {

	private CometdServer cometdServer;

	public void init() throws ServletException {
		Bayeux bayeux = (Bayeux) getServletContext().getAttribute(Bayeux.ATTRIBUTE);
		getServletContext().setAttribute(CometdServer.ATTRIBUTE, cometdServer = new CometdServer(bayeux));
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String clientId = request.getParameter("clientId");
//		System.err.println("request.getParameter(clientId)=" + clientId);
		if (clientId != null) {
			cometdServer.diconnect(clientId);
		}
	}
}