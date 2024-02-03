package com.tripbook.main.global.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tripbook.main.global.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findByRefId(long refId);

	Optional<List<Image>> findByRefIdAndRefType(Long refId, String refType);

	@Modifying
	@Query("update Image i set i.refId=null where i.refId=:refId")
	void updateByRefIdReset(@Param(value = "refId") long refId);

}
