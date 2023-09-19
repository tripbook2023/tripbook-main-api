package com.tripbook.main.global.util.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscordBotUtil {
    private final JDA jda;

    public DiscordBotUtil(DiscordBotToken discordBotToken) {
        String k = discordBotToken.getDiscordBotToken();
        this.jda = JDABuilder.createDefault(k)
                .setActivity(Activity.playing("에러 안 나길 기도"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new DiscordListener())
                .build();
    }

    public void sendMessage(String object) {
        System.out.println(jda.getGuilds());
        Guild guild = jda.getGuilds().get(0);
        List<GuildChannel> channels = guild.getChannels();
        TextChannel tc = (TextChannel) channels.stream().filter(c -> c.getName().contains("서버에러")).toList().get(0);

        tc.sendMessage(object).queue();
    }
}
