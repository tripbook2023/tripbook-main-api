package com.tripbook.main.global.repository;

import java.util.List;

import com.tripbook.main.global.entity.Image;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.token.enums.DeviceValue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findByRefId(long refId);


	@Query("update Image i set i.refId=null where i.refId=:refId")
	 long updateByRefIdReset(@Param(value = "refId") long refId);

}
