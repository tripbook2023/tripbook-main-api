package com.tripbook.main.global.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbook.main.global.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
