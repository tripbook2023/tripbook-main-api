package com.tripbook.main.token.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_JWTTK")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String token;

	public void updateToken(String token) {
		this.token = token;
	}

	public JwtToken(String token) {
		this.token = token;
	}

}
