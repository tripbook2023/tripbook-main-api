package com.tripbook.main.global.util.discord;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class DiscordListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        log.info("Dicord Bot is Ready");
    }

}
