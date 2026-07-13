package com.dugian.warp_drive.search.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api")
public class SearchController {

	@GetMapping("/hello")
	public String hello() {
		return "Warp Drive";
	}
}
