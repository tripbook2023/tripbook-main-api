package com.tripbook.main.member.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MemberStatus {
	@Schema(title = "정상계정")
	STATUS_NORMAL("STATUS_NORMAL"),
	@Schema(title = "정지계정")
	STATUS_SUSPEND("STATUS_SUSPEND"),
	@Schema(title = "탈퇴계정")
	STATUS_WITHDRAWAL("STATUS_WITHDRAWAL"),
	@Schema(title = "휴면계정")
	STATUS_SLEEP("STATUS_SLEEP"),
	@Schema(title = "추가인증필요 계정")
	ADDITIONAL_AUTHENTICATION("ADDITIONAL_AUTHENTICATION");
	private final String value;

}
