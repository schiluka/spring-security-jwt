package com.spring.mvc.jwt.config;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.spring.mvc.jwt.service.MyAppUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private MyAppUserDetailsService myAppUserDetailsService;	
	
	@Autowired
	private MyAuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/app/secure/**").hasAnyAuthority("ADMIN", "USER")
		.and().formLogin()  //login configuration
        .loginPage("/app/login")
        .loginProcessingUrl("/app-login").successHandler(authenticationSuccessHandler)
        //.defaultSuccessUrl("/app/secure/home", true).successHandler(authenticationSuccessHandler)	
		//.and().addFilter(filter)
		.and().logout()    //logout configuration
		.logoutUrl("/app-logout") 
		.logoutSuccessUrl("/app/login")//.addLogoutHandler(logoutHandler()).clearAuthentication(true)
		.deleteCookies("mycookie", "JSESSIONID").invalidateHttpSession(true);
	} 
	
    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	//BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        auth.userDetailsService(myAppUserDetailsService);//.passwordEncoder(passwordEncoder);
        //auth.authenticationProvider(authenticationProvider);
	}
    
    @Bean
    public NoOpPasswordEncoder passwordEncoder() {
    	return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
    
	@Bean
	public LogoutHandler logoutHandler() {
		return new LogoutHandler() {
			@Override
			public void logout(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) {
				for (Cookie cookie : request.getCookies()) {
					String cookieName = cookie.getName();
					Cookie cookieToDelete = new Cookie(cookieName, null);
					cookieToDelete.setMaxAge(0);
					response.addCookie(cookieToDelete);
				}
			}

		};
	}
	
	public SimpleUrlAuthenticationFailureHandler getAuthFailureHandler() {
		SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler("/app/login");
		handler.setDefaultFailureUrl("/app/login");
		return handler;
	}
}