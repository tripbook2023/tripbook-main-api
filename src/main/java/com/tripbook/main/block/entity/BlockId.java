package com.tripbook.main.block.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class BlockId implements Serializable {
	private Long memberId;
	private Long targetId;
}
