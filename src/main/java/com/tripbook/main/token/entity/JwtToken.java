package com.tripbook.main.token.entity;

import com.tripbook.main.global.common.BasicEntity;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.token.enums.DeviceValue;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_JWTTK")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtToken extends BasicEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String token;

	@Column(nullable = false)
	private DeviceValue device;
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "member_id", nullable = false)
	private Member memberId;

	public void updateToken(String token) {
		this.token = token;
	}

	public JwtToken(String token) {
		this.token = token;
	}

}
