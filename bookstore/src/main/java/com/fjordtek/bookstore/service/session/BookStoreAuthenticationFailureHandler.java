//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.service.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fjordtek.bookstore.service.HttpServerLogger;

/**
*
* This class implements Spring Framework security AuthenticationFailureHandler
* interface with specific method overrides.
* <p>
* Main purpose is to properly handle invalid authentication requests.
* <p>
* Additional request attributes are being delivered to /autherror POST end point.
*
* @see com.fjordtek.bookstore.web.BookController
*
* @author Pekka Helenius
*/

public class BookStoreAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private HttpServerLogger httpServerLogger = new HttpServerLogger();

	@Override
	public void onAuthenticationFailure(
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			AuthenticationException exception
			) throws IOException, ServletException {

		responseData.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpServerLogger.log(requestData, responseData);

		requestData
			.setAttribute("username", requestData.getParameter("b_username"));

		requestData
			.setAttribute("authfailure", "Authentication failure!");

		requestData.getRequestDispatcher("/autherror")
			.forward(requestData, responseData);

	}

}
