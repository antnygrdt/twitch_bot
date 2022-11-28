package fr.tsuyo.twitchbot;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import fr.tsuyo.twitchbot.command.CommandManager;
import fr.tsuyo.twitchbot.managers.Registration;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.EnumSet;

public class Main {

    public static Main instance;

    private final Dotenv config;
    private final ShardManager shardManager;
    private final TwitchClient twitchClient;

    private final CommandManager commandManager;

    public final String PREFIX;

    @SuppressWarnings("ConstantConditions")
    public Main() throws LoginException, IOException {
        instance = this;

        config = Dotenv.configure().load();
        String twitchToken = config.get("TWITCH_TOKEN");
        String discordToken = config.get("DISCORD_TOKEN");

        PREFIX = getConfig().get("PREFIX");
        commandManager = new CommandManager();

        shardManager = DefaultShardManagerBuilder.createDefault(discordToken)
                .enableIntents(EnumSet.allOf(GatewayIntent.class))
                .build();

        OAuth2Credential credential = new OAuth2Credential("twitch", twitchToken);
        twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withDefaultAuthToken(credential)
                .withDefaultEventHandler(SimpleEventHandler.class)
                .withEnableChat(true)
                .withChatAccount(credential)
                .build();

        twitchClient.getClientHelper().enableStreamEventListener(config.get("TWITCH_NAME"));
        twitchClient.getChat().joinChannel(config.get("TWITCH_NAME"));

        new Registration(shardManager).register();
    }

    public Dotenv getConfig() {
        return config;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public TwitchClient getTwitchClient() {
        return twitchClient;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public static void main(String[] args) {
        try {
            new Main();
        } catch (LoginException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}