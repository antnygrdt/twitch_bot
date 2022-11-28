package fr.tsuyo.twitchbot.command.commands;

import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.enums.CommandPermission;
import fr.tsuyo.twitchbot.command.ICommand;
import fr.tsuyo.twitchbot.managers.TwitchRequest;

import java.util.Arrays;
import java.util.List;

public class TitleCommand implements ICommand {
    @Override
    public void run(ChannelMessageEvent event, String[] args) {
        TwitchChat chat = event.getTwitchChat();
        String channel = event.getChannel().getName();

        if(!(event.getPermissions().contains(CommandPermission.MODERATOR) || event.getPermissions().contains(CommandPermission.BROADCASTER))) return;
        if(args.length < 1){ event.reply(event.getTwitchChat(), "Veuillez saisir un titre."); return;}

        StringBuilder title = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            title.append(args[i]);
            if(i+1 != args.length) title.append(" ");
        }

        new TwitchRequest().setTitle(title.toString()).send_patch();
        chat.sendMessage(channel, "/me Le titre a été changé pour: '" + title + "' !");
    }

    @Override
    public String name() {
        return "settitle";
    }

    @Override
    public List<String> aliases() {
        return Arrays.asList("title", "titre");
    }
}