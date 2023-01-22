package com.example.first;


import java.util.ArrayList;
import java.util.List;

import com.example.first.config.KafakaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@Autowired
	KafakaProducer kafakaProducer;


	@GetMapping(path = "/showStudents")
	public ResponseEntity<String> ShowStudents(@RequestParam("name") String name) {
		kafakaProducer.sendMessage(name);
		return ResponseEntity.ok("Worked perfectly " + name);
	}

}
