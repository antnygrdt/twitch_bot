package fr.tsuyo.twitchbot.managers;

import com.google.gson.JsonObject;
import fr.tsuyo.twitchbot.Main;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

public class TwitchCRequest {

    private final JsonObject body;

    private final String CLIENT_ID;
    private final String USER_ACCESS_TOKEN;
    private final String BROADCASTER_ID;
    private final String MODERATOR_ID;

    public TwitchCRequest() {
        Dotenv config = Main.instance.getConfig();
        CLIENT_ID = config.get("TWITCH_CLIENT_ID");
        USER_ACCESS_TOKEN = config.get("TWITCH_CHAT_SETTINGS_TOKEN");
        BROADCASTER_ID = config.get("TWITCH_USER_ID");
        MODERATOR_ID = config.get("TWITCH_BOT_ID");

        body = new JsonObject();
    }

    public TwitchCRequest setSlow(boolean bool){
        body.addProperty("slow_mode", bool);
        return this;
    }

    public TwitchCRequest setSlowDuration(int seconds){
        body.addProperty("slow_mode_wait_time", seconds);
        return this;
    }

    public TwitchCRequest setFollower(int seconds){
        body.addProperty("follower_mode", seconds);
        return this;
    }

    public TwitchCRequest setFollowerDuration(int minutes){
        body.addProperty("follower_mode_duration", minutes);
        return this;
    }

    public TwitchCRequest setSubscriber(boolean bool){
        body.addProperty("subscriber_mode", bool);
        return this;
    }

    public TwitchCRequest setEmote(boolean bool){
        body.addProperty("emote_mode", bool);
        return this;
    }

    public void send_patch(){
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody payload = RequestBody.create(body.toString(), mediaType);
            Request request = new Request.Builder()
                    .url(String.format("https://api.twitch.tv/helix/chat/settings?broadcaster_id=%s&moderator_id=%s", BROADCASTER_ID, MODERATOR_ID))
                    .patch(payload)
                    .addHeader("Authorization", "Bearer " + USER_ACCESS_TOKEN)
                    .addHeader("Client-ID", CLIENT_ID)
                    .build();

            client.newCall(request).execute().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}