package com.tripbook.main.global.entity;

import java.util.Date;

import com.tripbook.main.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_GIFTSHOP")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GiftShop {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name="member_id")
	private Member memberId;
	@Column
	@Setter
	private Long targetMemberId;
}
