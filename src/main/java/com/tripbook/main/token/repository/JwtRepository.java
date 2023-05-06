package com.tripbook.main.token.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbook.main.member.entity.Member;
import com.tripbook.main.token.entity.JwtToken;
import com.tripbook.main.token.enums.DeviceValue;

public interface JwtRepository extends JpaRepository<JwtToken, Long> {
	public Long deleteByDeviceAndMemberId(DeviceValue deviceValue, Member memberId);
}