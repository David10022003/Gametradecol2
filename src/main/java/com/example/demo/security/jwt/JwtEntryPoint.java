package com.example.demo.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
	
	private final static Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);
	
	@Override
	public void commence(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
			AuthenticationException authException) throws IOException, javax.servlet.ServletException {
		logger.error("fail en el metodo commence");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "no autorizado");
		
	}

}
