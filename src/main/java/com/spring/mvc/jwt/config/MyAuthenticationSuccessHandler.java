package com.spring.mvc.jwt.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.spring.mvc.jwt.service.JwtUtils;

/**
 * Spring invokes this after username/password is successfully validation
 * Adds cookie into response
 * 
 * 
 * @author schiluka
 *
 */
@Component
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final Log log = LogFactory.getLog(this.getClass());
	
	//@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		log.info("inside auth success handler:" + authentication);

		UsernamePasswordAuthenticationToken upAuthenticaton = (UsernamePasswordAuthenticationToken) authentication;
		User user = (User) upAuthenticaton.getPrincipal();
		String jwtCookie = JwtUtils.createToken(user.getUsername(), user.getAuthorities());
		//Create JWT token
		Cookie sessionCookie = new Cookie("mycookie", jwtCookie);
		response.addCookie(sessionCookie);

		// Redirect to home
		super.setAlwaysUseDefaultTargetUrl(true);
		super.setDefaultTargetUrl("/app/secure/home");
		super.onAuthenticationSuccess(request, response, authentication);
	}
	

}