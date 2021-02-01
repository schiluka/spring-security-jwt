package com.spring.mvc.jwt.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.mvc.jwt.service.JwtUtils;
import com.spring.mvc.jwt.service.MyAppUserDetailsService;

@Component
public class CookieValidationFilter extends OncePerRequestFilter {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private MyAppUserDetailsService myAppUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		Cookie[] cookies = request.getCookies();
		String jwtCookie = null;
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("mycookie")) {
				jwtCookie = cookie.getValue();
			}
		}
		String userName = JwtUtils.validateTokenAndUserName(jwtCookie);
		if(userName == null ||
				myAppUserDetailsService.loadUserByUsername(userName) == null) {
			log.error("invalid user credentials - redirecting to login screen");
			response.sendRedirect("/app/login");
		}
		filterChain.doFilter(request, response);
	}


	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		if(!request.getRequestURI().contains("app/secure")) {
			return true;
		}
		return false;
	}
}
