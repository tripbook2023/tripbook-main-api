package com.tripbook.main.member.entity;

import java.util.Date;

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
import lombok.Setter;

@Entity
@Table(name = "TB_MEMBER_GRADE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGradeHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member memberId;

	@Setter
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MemberRole role;

	@Setter
	@Column(nullable = false)
	private Date createdAt;
	@Setter
	@Column(nullable = false)
	private Date createdBy;

}
