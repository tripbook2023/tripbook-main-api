package com.tripbook.main.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbook.main.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Member findByEmail(String email);

	Optional<Member> findOptionalByEmail(String email);

	Member findByName(String name);

}
