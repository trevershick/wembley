package com.railinc.r2dq.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/errors")
public class ErrorController {

	@RequestMapping("/nosession")
	public String nosession() {
		return "errors/nosession";
	}
}
