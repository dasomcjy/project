package com.mysite.member;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.DataNotFoundException;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class MemberService {
	
	public Member getmember(String username) {
		
		Optional<Member> member = this.memberRepository.findByUsername(username);
		
		if(member.isPresent()) {
			
			return member.get();
			
		} else {
			
			throw new DataNotFoundException("member not found");
		}
	}

	private final MemberRepository memberRepository;
	
	private final PasswordEncoder passwordEncoder;
	
		public Member create(String username, String email, String password) {
			
			Member member = new Member();
			
			member.setUsername(username);
			
			member.setEmail(email);
			
			member.setPassword(passwordEncoder.encode(password));
			
			this.memberRepository.save(member);
			
			return member;
		}
	
}
