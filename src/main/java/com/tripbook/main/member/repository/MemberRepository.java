package com.tripbook.main.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbook.main.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Member findByEmail(String email);

	Member findByName(String name);

	Integer deleteByEmail(String email);
}
