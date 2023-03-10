package com.mysite.member;

import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {
	private final MemberService memberService;

	@GetMapping("/login")
	public String login() {
		
		return "login";
	}
	
	
	@GetMapping("/join")

	public String join(MemberDTO memberDTO) {

		return "join";

	}
	



	@PostMapping("/join")
    public String join(@Valid MemberDTO memberDTO, BindingResult bindingResult) {

	 if (bindingResult.hasErrors()) {
 
		 return "join";
 }
 
	 if (!memberDTO.getPassword1().equals(memberDTO.getPassword2())) {
 
		 bindingResult.rejectValue("password2", "passwordInCorrect",
 
				 "2 개의 패스워드가 일치하지 않습니다.");
 
		 return "join";

	 }

	 
	 try {
		 
		 memberService.create(memberDTO.getUsername(),memberDTO.getEmail(), memberDTO.getPassword1());
	 		
	 	}catch(DataIntegrityViolationException e) {
	 		
	 		e.printStackTrace();
	 		
	 		bindingResult.reject("signupFailed" , "이미 등록된 사용자입니다.");
	 		
	 		return "join";
	 		
	 	}catch(Exception e) {
	 		
	 		e.printStackTrace();
	 		
	 		bindingResult.reject("signupFailed" , e.getMessage());
	 		
	 		return "join";
	 
	 	}
	
 
	 return "redirect:/index";
 }
	
}
