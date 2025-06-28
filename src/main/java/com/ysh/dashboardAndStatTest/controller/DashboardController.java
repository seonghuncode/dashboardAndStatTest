package com.ysh.dashboardAndStatTest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DashboardController {

	@RequestMapping(value = "/Dashboard", method = RequestMethod.GET)
	public String dashboard() {
		return "dashboard/dashboard";
	}
	
}
