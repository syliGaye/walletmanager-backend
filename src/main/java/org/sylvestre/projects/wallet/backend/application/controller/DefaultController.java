package org.sylvestre.projects.wallet.backend.application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DefaultController {
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);
	
	@GetMapping("/api")
	public	@ResponseBody String ping() {
		logger.info("Réponse du serveur sur /api : "+ HttpStatus.OK);
    	return "{\"status\":\"OK\",\"timestamp\":\"" + System.currentTimeMillis() + "\"}";
	}

}