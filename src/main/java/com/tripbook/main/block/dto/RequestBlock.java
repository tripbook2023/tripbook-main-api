package com.tripbook.main.block.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tripbook.main.member.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestBlock {
	@Schema(description = "차단대상자Id")
	private List<Long> targetIdList;
	@Schema(description = "사용자 Id")
	@JsonIgnore
	private Member member;

}
