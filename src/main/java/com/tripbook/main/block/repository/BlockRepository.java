package com.tripbook.main.block.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbook.main.block.entity.Block;
import com.tripbook.main.member.entity.Member;

public interface BlockRepository extends JpaRepository<Block, Long> {
	// 차단 사용자 조회
	List<Block> findBlockByMemberId(Member memberId);

	Long countByMemberIdAndTargetId(Member memberId, Long targetId);

	// 차단해제
	Long deleteAllByTargetIdInAndMemberId(List<Long> targetId, Member memberId);
}
