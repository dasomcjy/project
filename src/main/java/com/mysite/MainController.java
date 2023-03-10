package com.mysite;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

	@GetMapping("/index")
	public String index() {
		
		return "index";
	}
	
	@GetMapping("/sub1")
	public String sub1() {
			
			return "sub1";
			
		}
	
	@GetMapping("/sub2")
	public String sub2() {
			
			return "sub2";
		
	}
	
	@GetMapping("/sub3")
	public String sub3() {
			
			return "sub3";
	}
	
	@GetMapping("/ggg")
	public String wr() {
			
			return "write";
	}
	
	
	
//	@GetMapping("/sub4")
//	public String sub4() {
//			
//			return "sub4";
//	}
//	
//	@GetMapping("/write")
//	public String write() {
//			
//			return "write";
//	}
//	
//	@GetMapping("/freewrite")
//	public String freewrite() {
//			
//			return "freewrite";
//	}
//	

}
	

