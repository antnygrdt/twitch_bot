package fr.tsuyo.twitchbot.listeners;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import fr.tsuyo.twitchbot.Main;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Arrays;

public class ChatListener {

    private final Dotenv config = Main.instance.getConfig();

    public ChatListener(SimpleEventHandler eventHandler) {
        eventHandler.onEvent(ChannelMessageEvent.class, this::onChannelMessage);
    }

    public void onChannelMessage(ChannelMessageEvent event){
        if(event.getUser().getName().equalsIgnoreCase(config.get("TWITCH_BOT_NAME")) || !event.getChannel().getName().equalsIgnoreCase(config.get("TWITCH_NAME"))) return;
        if(event.getMessage().substring(0, 1).equalsIgnoreCase(Main.instance.PREFIX)){
            String[] array = event.getMessage().split(" "), args = Arrays.copyOfRange(array, 1, array.length);
            Main.instance.getCommandManager().handle(event, args);
        }
    }
}