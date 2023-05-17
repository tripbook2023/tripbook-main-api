package com.tripbook.main.global.common;

public enum TripBookErrorCode {
	USER_ALREADY_EXISTS(3000);

	private final int value;

	TripBookErrorCode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
