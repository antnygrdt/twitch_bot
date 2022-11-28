package fr.tsuyo.twitchbot.managers;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import fr.tsuyo.twitchbot.Main;
import fr.tsuyo.twitchbot.listeners.ChatListener;
import fr.tsuyo.twitchbot.listeners.MessageReceivedListener;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Registration {

    private final ShardManager shardManager;
    public Registration(ShardManager shardManager) {
        this.shardManager = shardManager;
    }

    public void register(){
        shardManager.addEventListener(new MessageReceivedListener());
        SimpleEventHandler eventHandler = Main.instance.getTwitchClient().getEventManager().getEventHandler(SimpleEventHandler.class);

        new ChatListener(eventHandler);
    }
}