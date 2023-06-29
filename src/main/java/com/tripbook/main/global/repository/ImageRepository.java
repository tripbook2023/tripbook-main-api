package com.tripbook.main.global.repository;

import com.tripbook.main.global.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
