package fr.tsuyo.twitchbot.command.commands;

import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.enums.CommandPermission;
import fr.tsuyo.twitchbot.command.ICommand;
import fr.tsuyo.twitchbot.managers.TwitchGameInfo;
import fr.tsuyo.twitchbot.managers.TwitchRequest;

import java.util.Collections;
import java.util.List;

public class GameCommand implements ICommand {
    @Override
    public void run(ChannelMessageEvent event, String[] args) {
        TwitchChat chat = event.getTwitchChat();
        String channel = event.getChannel().getName();

        if(!(event.getPermissions().contains(CommandPermission.MODERATOR) || event.getPermissions().contains(CommandPermission.BROADCASTER))) return;
        if(args.length < 1){ event.reply(event.getTwitchChat(), "Veuillez saisir un jeu."); return;}

        StringBuilder game = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            game.append(args[i]);
            if(i+1 != args.length) game.append(" ");
        }

        TwitchRequest tR = new TwitchRequest();
        TwitchGameInfo gameInfo = tR.getGameInfo(game.toString());
        if(gameInfo == null){
            event.reply(chat, "Ce jeu est inconnu.");
        }else{
            tR.setGame(gameInfo.getID()).send_patch();
            chat.sendMessage(channel, "/me Le jeu a été changé pour: '" + gameInfo.getName() + "' !");
        }
    }

    @Override
    public String name() {
        return "setgame";
    }

    @Override
    public List<String> aliases() {
        return Collections.singletonList("game");
    }
}
