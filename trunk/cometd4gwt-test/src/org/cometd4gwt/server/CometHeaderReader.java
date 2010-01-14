package org.cometd4gwt.server;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class CometHeaderReader implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException {

//		System.err.println("filter calling for " + request);
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		System.err.println("servletRequest.getRequestURL()=" + servletRequest.getRequestURL());
		System.err.println("servletRequest.getHeader(userId)=" + servletRequest.getHeader("userId"));
		System.err.println("servletRequest.getHeader(name)=" + servletRequest.getHeader("name"));

//		Enumeration<?> e = servletRequest.getHeaderNames();
//		while (e.hasMoreElements()) {
//			Object headersName = e.nextElement();
//			System.out.println(headersName + "=" + servletRequest.getHeader("" + headersName));
//		}

		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}
}
