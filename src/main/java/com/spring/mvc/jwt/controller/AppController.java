package com.spring.mvc.jwt.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("app")
public class AppController {

	private static final Logger log = LogManager.getLogger(AppController.class);
	
	@GetMapping("login")
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login");
		return mav;
	}

	@GetMapping("secure/home")
	public ModelAndView home() {
		log.debug("inside secure/home controller");
		ModelAndView mav = new ModelAndView();
		// mav.addObject("userArticles", userInfoService.getAllUserArticles());
		mav.setViewName("home");
		return mav;
	}

	@RequestMapping("/error")
	@ResponseBody
	public String handleError(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
		exception.printStackTrace();
		return String.format(
				"<html><body><h2>Error Page</h2><div>Status code: <b>%s</b></div>"
						+ "<div>Exception Message: <b>%s</b></div><body></html>",
				statusCode, exception == null ? "N/A" : exception.getMessage());
	}

}