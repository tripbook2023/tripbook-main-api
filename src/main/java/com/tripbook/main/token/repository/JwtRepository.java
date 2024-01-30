package com.tripbook.main.token.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tripbook.main.member.entity.Member;
import com.tripbook.main.token.entity.JwtToken;
import com.tripbook.main.token.enums.DeviceValue;

public interface JwtRepository extends JpaRepository<JwtToken, Long> {
	public Long deleteByDeviceAndMemberId(DeviceValue deviceValue, Member memberId);
	public long deleteByMemberId(Member member);

	public List<JwtToken> findByDeviceAndMemberId(DeviceValue deviceValue, Member memberId);

	@Query("select tm  from JwtToken jwt JOIN Member tm WHERE tm.email = :email and tm.isEnable=true and jwt.device=:device")
	public List<Member> findByEmail(@Param(value = "email") String email,
		@Param(value = "device") DeviceValue device);
}