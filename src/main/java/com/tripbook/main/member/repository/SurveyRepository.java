package com.tripbook.main.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbook.main.member.entity.Survey;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
