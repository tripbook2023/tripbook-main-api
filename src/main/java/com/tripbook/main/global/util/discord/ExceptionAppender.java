package com.tripbook.main.global.util.discord;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@AutoConfigurationPackage
@Profile({"prod"})
public class ExceptionAppender {

	private static final String DISCORD_ALARM_FORMAT = "[DiscordAlarm] %s";

	private final RequestStorage requestStorage;
	private final DiscordMessageGenerator discordMessageGenerator;
	private final DiscordBotUtil discordBotUtil;

	public ExceptionAppender(RequestStorage requestStorage,
		DiscordMessageGenerator discordMessageGenerator,
		DiscordBotUtil discordBotUtil) {
		this.requestStorage = requestStorage;
		this.discordMessageGenerator = discordMessageGenerator;
		this.discordBotUtil = discordBotUtil;
	}

	@Before("@annotation(com.tripbook.main.global.util.discord.DiscordAlarm)")
	public void appendExceptionToResponseBody(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		if (!validateHasOneArgument(args)) {
			return;
		}

		if (!validateIsException(args)) {
			return;
		}

		String message = discordMessageGenerator
			.generate(requestStorage.get(), (Exception)args[0]);
		discordBotUtil.sendMessage(message);
	}

	private boolean validateIsException(Object[] args) {
		if (!(args[0] instanceof Exception)) {
			log.warn("[SlackAlarm] argument is not Exception");
			return false;
		}

		return true;
	}

	private boolean validateHasOneArgument(Object[] args) {
		if (args.length != 1) {
			log.warn(String
				.format(DISCORD_ALARM_FORMAT, "ambiguous exceptions! require just only one Exception"));
			return false;
		}

		return true;
	}
}
