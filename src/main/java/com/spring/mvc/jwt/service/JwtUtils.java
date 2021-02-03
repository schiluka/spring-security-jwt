package com.spring.mvc.jwt.service;

import java.util.Collection;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtils {

	private static final Logger log = LogManager.getLogger(JwtUtils.class);
	
	public static String createToken(String username, 
			Collection<? extends GrantedAuthority> roles) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("user_name", username);

		Claims claims = Jwts.claims().setSubject(jsonObject.toString());
		Date now = new Date();
		Date validity = new Date(now.getTime() + 3600000); // 1hr validity
		String token = Jwts.builder()//
				.setClaims(claims)//
				.setIssuedAt(now)//
				.setExpiration(validity)//
				.signWith(SignatureAlgorithm.HS256, "secretKey")//
				.compact();
		return token;
	}
	
	public static String validateTokenAndUserName(String jwtCookie) {
		
		Claims claim = Jwts.parser()
                    .setSigningKey("secretKey")
                    .parseClaimsJws(jwtCookie)
                    .getBody();
		Date expDate = claim.getExpiration();
		Date now = new Date();
		Date validity = new Date(now.getTime() - 3600000);
		if(expDate.before(validity)) {
			log.info("token expired");
			return null;
			
		}
		log.debug("getSubject" + claim.getSubject());
		
		JSONObject jsonObject = new JSONObject(claim.getSubject());
		if(jsonObject.has("user_name")) {
			String userName = jsonObject.get("user_name").toString();
			//Validate user name from user details service
			log.debug("token validated");
			return userName;
		}
		log.debug("token not validated");
		return null;
	}

}
