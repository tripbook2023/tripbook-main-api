package com.tripbook.main.search.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbook.main.search.entity.Search;

public interface SearchRepository extends JpaRepository<Search, Long> {
	public Search findByContent(String content);
}
