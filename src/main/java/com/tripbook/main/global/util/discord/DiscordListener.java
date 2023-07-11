package com.tripbook.main.global.util.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscordListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println(event.getGuildAvailableCount());
        Guild guild = event.getJDA().getGuilds().get(0);
        System.out.println(guild);
        System.out.println(guild.getChannels().get(0).getName());
        List<GuildChannel> channels = guild.getChannels();
        TextChannel d = (TextChannel) channels.stream().filter(c -> c.getName().equals("일반")).toList().get(0);
        d.sendMessage("adf").queue();
    }

}
