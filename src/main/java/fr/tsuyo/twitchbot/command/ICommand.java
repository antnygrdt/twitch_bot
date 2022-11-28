package fr.tsuyo.twitchbot.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import java.util.Collections;
import java.util.List;

public interface ICommand {

      void run(ChannelMessageEvent event, String[] args);

      String name();

      default List<String> aliases() { return Collections.emptyList(); }
}
