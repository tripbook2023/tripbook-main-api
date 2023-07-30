package com.tripbook.main.global.util.discord;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DiscordBotToken {
    @Value("${discord.bot.token}")
    private String token;

    public String getDiscordBotToken() {
        return token;
    }
}
