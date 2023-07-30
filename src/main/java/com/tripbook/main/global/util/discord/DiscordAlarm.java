package com.tripbook.main.global.util.discord;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DiscordAlarm {
    // @ExceptionHandler 가 붙어있는 메서드에 같이 사용
}
