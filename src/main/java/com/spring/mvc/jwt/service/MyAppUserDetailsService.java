package com.spring.mvc.jwt.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.mvc.jwt.dto.UserInfo;

@Service
public class MyAppUserDetailsService implements UserDetailsService {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	private static Map<String, UserInfo> userMap = new HashMap<>();
	
	static {
		UserInfo user1 = new UserInfo();
		user1.setFullName("USER1");
		user1.setUserName("user1");
		user1.setPassword("{bcrypt}user1");
		user1.setRole("USER");
		userMap.put("user1", user1);
		
		UserInfo user2 = new UserInfo();
		user2.setFullName("USER2");
		user2.setUserName("user2");
		user2.setPassword("{bcrypt}user2");
		user2.setRole("USER");
		userMap.put("user2", user2);
	}
	
	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		UserInfo activeUserInfo = userMap.get(userName); 
		if(activeUserInfo == null) {
			log.error("invalid user credentials - redirecting to login screen");
			throw new UsernameNotFoundException("invalid user credentials");
		}
		GrantedAuthority authority = new SimpleGrantedAuthority(activeUserInfo.getRole());
		UserDetails userDetails = (UserDetails)new User(activeUserInfo.getUserName(),
				activeUserInfo.getPassword(), Arrays.asList(authority));
		log.info("found user details");
		return userDetails;
	}
}

