package com.mysite.answer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	
	//  JPA 에서 사용가능한 메소드 상속됨 
		// findAll() 
		// findById() 
		// save()
		// delete()
	
	
}
