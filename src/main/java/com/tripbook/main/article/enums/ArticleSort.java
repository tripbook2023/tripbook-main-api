package com.tripbook.main.article.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ArticleSort {
	CREATED_ASC("createdAsc"),
	CREATED_DESC("createdDesc"),
	POPULARITY("popularity");
	@Getter
	private final String value;
}


