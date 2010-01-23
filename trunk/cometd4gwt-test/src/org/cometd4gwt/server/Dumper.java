package org.cometd4gwt.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cometd.Bayeux;
import org.cometd.Channel;
import org.cometd.Client;

public class Dumper extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CometdServer cometdServer;

	@Override
	public void init() throws ServletException {
		cometdServer = (CometdServer) getServletContext().getAttribute(CometdServer.ATTRIBUTE);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		writer.println(getDump());
		writer.close();
	}

	private String getDump() {
		StringBuffer buf = new StringBuffer();
		Bayeux bayeux = cometdServer.getBayeux();

		buf.append(bayeux.getClients().size() + " clients - ");
		for (Client c : bayeux.getClients()) {
			buf.append(", " + c);
		}

		buf.append("\n" + bayeux.getClients().size() + " channels - ");
		for (Channel c : bayeux.getChannels()) {
			buf.append(", " + c + " (" + c.getSubscribers() + ")");

		}

		return buf.toString();
	}
}