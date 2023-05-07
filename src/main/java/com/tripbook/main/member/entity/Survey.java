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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
여행지, 여행기간, 동행인, 여행비용, 여행스타일)
 */
@Entity
@Table(name = "TB_SURVEY")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	@JoinColumn(name = "member_id")
	private Member memberId;
	@Column(nullable = false)
	private String location;
	@Column(nullable = false)
	private String period;
	@Column(nullable = false)
	private String accompany;
	@Column(nullable = false)
	private String transportation;
	@Column(nullable = false)
	private String purpose;

	@Builder
	public Survey(Member memberId, String location, String period, String accompany, String transportation,
		String purpose) {
		this.memberId = memberId;
		this.location = location;
		this.period = period;
		this.accompany = accompany;
		this.transportation = transportation;
		this.purpose = purpose;
	}
}
