package com.tripbook.main.global.util.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscordBotUtil {
    private final DiscordBotToken discordBotToken;
    private final JDA jda;
    @Value("${discord.bot.token}")
    private String token;

    public DiscordBotUtil(DiscordBotToken discordBotToken) {
        this.discordBotToken = discordBotToken;
        String k = discordBotToken.getDiscordBotToken();
        this.jda = JDABuilder.createDefault(k)
                .setActivity(Activity.playing("메세지 기다리기"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new DiscordListener())
                .build();
    }

    public void sendMessage(String object) {
        System.out.println(jda.getGuilds());
        Guild guild = jda.getGuilds().get(0);
        List<GuildChannel> channels = guild.getChannels();
        TextChannel tc = (TextChannel) channels.stream().filter(c -> c.getName().equals("일반")).toList().get(0);

        tc.sendMessage(object).queue();
    }
}
