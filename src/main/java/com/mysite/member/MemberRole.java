package com.mysite.member;

import lombok.Getter;

@Getter
public enum MemberRole {

	ADMIN("ROLE_ADMIN"),
	
	USER("RLOE_USER");
	
	
	MemberRole(String value) {
		
		this.value = value;
	}
	
	private String value;
}
