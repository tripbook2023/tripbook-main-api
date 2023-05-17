package com.tripbook.main.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MemberStatus {

	STATUS_NORMAL("STATUS_NORMAL"),
	STATUS_SUSPEND("STATUS_SUSPEND"),
	STATUS_WITHDRAWAL("STATUS_WITHDRAWAL"),
	STATUS_SLEEP("STATUS_SLEEP"),
	ADDITIONAL_AUTHENTICATION("ADDITIONAL_AUTHENTICATION");
	private final String value;

}
