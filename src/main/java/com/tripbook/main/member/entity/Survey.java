package com.tripbook.main.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
여행지, 여행기간, 동행인, 여행비용, 여행스타일)
 */
@Entity
@Table(name = "TB_SURVEY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	@JoinColumn(name = "member_id")
	private Member memberId;
	@Setter
	@Column(nullable = false)
	private String location;
	@Column(nullable = false)
	@Setter
	private String period;

	@Setter
	@Column(nullable = false)
	private String accompany;
	@Setter
	@Column(nullable = false)
	private Long accompanyCount;
	@Setter
	@Column(nullable = false)
	private Integer expense;
	@Setter
	@Column(nullable = false)
	private String transportation;
	@Setter
	@Column(nullable = false)
	private String purpose;
}
