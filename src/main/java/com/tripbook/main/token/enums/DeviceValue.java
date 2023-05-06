package com.tripbook.main.token.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeviceValue {

	TABLET("T"),
	MOBILE("M"),
	WEB("W");

	private final String value;

}
