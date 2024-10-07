package app.edlc.taskapi.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitialController {
	@GetMapping("/")
	public String check() {
		return "Server is up and running.";
	}
}
