package fr.tsuyo.twitchbot.command.commands;

import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.enums.CommandPermission;
import fr.tsuyo.twitchbot.command.ICommand;
import fr.tsuyo.twitchbot.managers.TwitchCRequest;

import java.util.Collections;
import java.util.List;

public class SubOffCommand implements ICommand {

    @Override
    public void run(ChannelMessageEvent event, String[] args) {
        TwitchChat chat = event.getTwitchChat();
        String channel = event.getChannel().getName();
        if(!(event.getPermissions().contains(CommandPermission.MODERATOR) || event.getPermissions().contains(CommandPermission.BROADCASTER))) return;

        new TwitchCRequest().setSubscriber(false).send_patch();
        chat.sendMessage(channel, "/me Désactivation du mode réservé aux abonnés.");
    }

    @Override
    public String name() {
        return "suboff";
    }

    @Override
    public List<String> aliases() {
        return Collections.singletonList("soff");
    }
}