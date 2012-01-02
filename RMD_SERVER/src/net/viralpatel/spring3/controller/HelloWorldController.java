package net.viralpatel.spring3.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@RequestMapping("/hello")
	public ModelAndView helloWorld() {
		logger.info("Return View");
		String message = "Hello World, Spring 3.0!";
		System.out.println(message);
		return new ModelAndView("hello", "message", message);
	}

}