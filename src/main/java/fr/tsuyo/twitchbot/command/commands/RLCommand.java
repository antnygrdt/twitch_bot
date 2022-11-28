package fr.tsuyo.twitchbot.command.commands;

import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import fr.tsuyo.twitchbot.command.ICommand;
import fr.tsuyo.twitchbot.stats.RocketStats;
import fr.tsuyo.twitchbot.stats.RocketStats.Games;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("FieldCanBeLocal")
public class RLCommand implements ICommand {

    private Long timestamp;
    private final int cooldown = 10;

    @Override
    public void run(ChannelMessageEvent event, String[] args) {
        TwitchChat chat = event.getTwitchChat();
        String channel = event.getChannel().getName();
        RocketStats stats = new RocketStats();

        String beggining, cd = "";
        if(timestamp != null && (System.currentTimeMillis() - timestamp) < TimeUnit.SECONDS.toMillis(cooldown))
            return;
        timestamp = System.currentTimeMillis();
        beggining = "Tsuyo est: ";
        cd += " [📛"+cooldown+"]";

        if(args.length >= 1){
            String finalCd = cd;
            Arrays.stream(Games.values()).filter(game -> game.getName().equalsIgnoreCase(args[0])).findFirst().ifPresent(game -> {
                try {
                    chat.sendMessage(channel, beggining + stats.getDisplay(game) + finalCd);
                } catch (Exception e) {
                    chat.sendMessage(channel, "Service indisponible.");
                }
            });
        }else{
            try {
                chat.sendMessage(channel, String.format("%s1v1: %s || 2v2: %s || 3v3: %s %s",
                        beggining, stats.getDisplay(Games.DUEL), stats.getDisplay(Games.DUO), stats.getDisplay(Games.TRIO), cd));
            } catch (Exception ignored) {
                chat.sendMessage(channel, "Service indisponible.");
            }
        }
    }

    @Override
    public String name() {
        return "rl";
    }

    @Override
    public List<String> aliases() {
        return Collections.singletonList("rocketleague");
    }
}
