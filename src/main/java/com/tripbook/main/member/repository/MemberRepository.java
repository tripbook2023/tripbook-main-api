package com.tripbook.main.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.enums.MemberStatus;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Member findByEmail(String email);

	Member findByName(String name);

	Integer deleteByEmail(String email);

	List<Member> findByNameContainsAndStatusEquals(String name, MemberStatus memberStatus);
}
