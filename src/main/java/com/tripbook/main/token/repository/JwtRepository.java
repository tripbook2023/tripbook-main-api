package com.tripbook.main.token.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbook.main.token.entity.JwtToken;

public interface JwtRepository extends JpaRepository<JwtToken, Long> {

}