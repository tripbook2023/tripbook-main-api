package com.tripbook.main.member.entity;

import com.tripbook.main.global.common.BasicEntity;
import com.tripbook.main.member.enums.MemberRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_MEMBER_GRADE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGradeHistory extends BasicEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member memberId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MemberRole role;

	public void updateStatus(MemberRole role) {
		this.role = role;
	}
}
