package com.tripbook.main.global.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.tripbook.main.global.common.BasicEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_IMAGE")
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
public class Image extends BasicEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 1000)
	private String url;
	//S3 Bucket in name
	@Column(nullable = false)
	private String keyName;
	@Column(nullable = false)
	private String name;

	@Column
	private Long refId;

	@Column
	private String refType;

	@Builder
	public Image(String url, String name, long refId, String refType, String keyName) {
		this.url = url;
		this.name = name;
		this.refId = refId;
		this.refType = refType;
		this.keyName = keyName;
	}

	@Builder
	public Image(String url, String name) {
		this.url = url;
		this.name = name;
	}

	public void updateRefId(Long refId) {
		this.refId = refId;
	}
}
