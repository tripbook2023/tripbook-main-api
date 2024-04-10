package com.tripbook.main.block.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.tripbook.main.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_BLOCK")
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(BlockId.class)
public class Block {
	//차단 요청자
	@Id
	@ManyToOne
	@JoinColumn(name = "memberId", updatable = false)
	private Member memberId;
	@Id
	@Column(nullable = false)
	//차단 대상자
	private Long targetId;

	public Block(Member memberId, Long targetId) {
		this.memberId = memberId;
		this.targetId = targetId;
	}

}
