package com.railinc.r2dq.support;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/s/support")
public class SupportController extends SupportControllerBaseImpl  {


	
	@RequestMapping
	public String list(Model map) {
		return "support/support";
	}
	
	@RequestMapping("properties")
	public String properties(ModelMap model) {
		model.addAttribute("properties", System.getProperties().entrySet());
		return "support/properties";
	}

}
