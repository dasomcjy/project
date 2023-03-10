package com.mysite.question;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.answer.Answer;
import com.mysite.member.Member;
import com.mysite.member.MemberService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final필드의 생성자를 자동으로 만들어서 생성자를 통한 의존성 주입
@Controller //
public class QuestionController {

	/*
	 * 클래스를 객체로 생성 어노테이션 ( 빈(객체) 등록 , Spring Framework)
	 * 
	 * @Component : 일반적인 클래스를 객체화
	 * 
	 * @Controller : 클라이언트 요청을 받아서 처리 , Controller 1. 클라이언트 요청을
	 * 받는다. @GetMapping, @PostMapping 2. 비즈니스 로직 처리 , Service의 메소드 호출, 3. View 페이지로
	 * 응답
	 * 
	 * @Service : DAO의 메소드를 인터페이스로 생성후 인터페이스의 메소드를 구현한 클래스 - 유지보수를 쉽게하기 위해서 (약결합)
	 * 
	 * @Repository : DAO 클래스를 빈등록
	 * 
	 */

	/*
	 * 
	 * DI ( 의존성 주입 ) 1. @Autowired : Spring Framework 의 생성된 빈(객체)을 주입, 타입을 찾아서 주입 같은
	 * 타입의 객체가 존재할 경우 문제가 발생될 수 있다. 2. 생성자를 통한 의존성 주입 (권장방식) 3. Setter를 사용한 의존성 주입
	 * 
	 * 
	 */

	// 생성자를 통한 의존성 주입 <== 권장하는 방식
	// Controller에서 직접 Repository를 접근하지 않고 Service를 접근하도록
	// private final QuestionRepository questionrepository;
	private final QuestionService questionService;

	private final MemberService memberService;



//	@GetMapping("/question/list")		//http://localhost:8181/question/list
//	@PostMapping("/question/list")		//Form 태그의 method=post action = "/question/list"
	// @ResponseBody //요청을 브라우저에 출력
//	public String list(Model model) {
	// 1. 클라이언트 요청 정보 : //http://localhost:8181/question/list

	// 2. 비즈니스 로직을 처리
//		List<Question> questionList = 
	// this.questionrepository.findAll();
//				this.questionService.getList();

	// 3. 뷰(view) 페이지로 전송
	// Model : 뷰페이지로 서버에 데이터를 담아서 전송 객체 ( Session, Application )

//		model.addAttribute("questionList", questionList);

//		return "question_list";

//	}

	
	

	// 2월 14일 페이징처리를 위해 수정됨
	// http://localhost:8181/question/list/?page=0
	@GetMapping("/sub4")
	 public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			  @RequestParam(value = "kw", defaultValue = "") String kw) {
			  Page<Question> paging = this.questionService.getList(page, kw);
			  model.addAttribute("paging", paging);
			  model.addAttribute("kw", kw);
			  return "sub4";
			  }

	// 상세 페이지를 처리하는 메소드 : /question/detail/1

	@GetMapping(value = "/question/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, Answer answerform) {

		// 서비스 클래스의 메소드 호출 : 상세페이지 보여달라
		Question q = this.questionService.getQuestion(id);

		// Model 객체에 담아서 클라이언트에게 전송
		model.addAttribute("question", q);

		return "freewritetext"; // template : question_detail.html

	}
	
	

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/question/create")
	public String questionCreate(QuestionForm questionForm) {
		return "freewrite";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/question/create")
	public String questionCreate(
			// @RequestParam String subject, @RequestParam String content
			@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {

		if (bindingResult.hasErrors()) { // subject, content 가 비어있을때
			return "freewrite";
		}
		
		//현재 로그온한 사용자정보를 확인해 보기 
		System.out.println("현재 로그온한 사용자 정보 : " + principal.getName());

		Member member = this.memberService.getmember(principal.getName());

		this.questionService.create(questionForm.getSubject(), questionForm.getContent(), member);
		// 로직 작성부분 ( Service에서 로직을 만들어서 작동 )
		// this.qustionService.create(subject, content);

		// this.questionService.create(questionForm.getSubject(),
		// questionForm.getContent());

		// this.questionService.create(questionForm.getSubject(),
		// questionForm.getContent());
		// 값을 DB에 저장후 List페이지로 리다이렉트 (질문 목록으로 이동)
		return "redirect:/sub4";

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/question/modify/{id}")
	public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		return "freewrite";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/question/modify/{id}")
	public String freewrite(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal,
			@PathVariable("id") Integer id) {
		if (bindingResult.hasErrors()) {
			return "freewrite";
		}
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
		return String.format("redirect:/question/detail/%s", id);
	}

	
	 @PreAuthorize("isAuthenticated()")
	 @GetMapping("/question/delete/{id}")
	 public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
	 Question question = this.questionService.getQuestion(id);
	 if (!question.getAuthor().getUsername().equals(principal.getName())) {
	 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
	 }
	 this.questionService.delete(question);
	 return "redirect:/sub4";
	 }
	 
	 //id : Question 객체
	 //principal : 현재 투표하는 객체를 가지고 온다.
	 
	 //2월 17일 : 질문에서 추천 기능 추가
	 @PreAuthorize("isAuthenticated()")
	 @GetMapping("/question/vote/{id}")
	 public String questionVote(Principal principal, @PathVariable("id") Integer id) {
	 Question question = this.questionService.getQuestion(id);
	 Member siteUser = this.memberService.getmember(principal.getName());
	 this.questionService.vote(question, siteUser);
	 return String.format("redirect:/question/detail/%s", id);
	 }
	 
	 
	 //2월 17일 : 답변에서 추천 기능 추가
}
