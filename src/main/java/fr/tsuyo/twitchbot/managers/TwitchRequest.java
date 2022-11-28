package fr.tsuyo.twitchbot.managers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.tsuyo.twitchbot.Main;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.*;

import javax.annotation.Nullable;
import java.io.IOException;

public class TwitchRequest {

    private final JsonObject body;

    private final String CLIENT_ID;
    private final String USER_ACCESS_TOKEN;
    private final String APP_ACCESS_TOKEN;
    private final String BROADCASTER_ID;

    public TwitchRequest() {
        Dotenv config = Main.instance.getConfig();
        CLIENT_ID = config.get("TWITCH_CLIENT_ID");
        USER_ACCESS_TOKEN = config.get("TWITCH_USER_ACCESS_TOKEN");
        APP_ACCESS_TOKEN = config.get("TWITCH_APP_ACCESS_TOKEN");
        BROADCASTER_ID = config.get("TWITCH_USER_ID");

        body = new JsonObject();
    }

    public TwitchRequest setTitle(String title){
        body.addProperty("title", title);
        return this;
    }

    public TwitchRequest setGame(String gameID){
        body.addProperty("game_id", gameID);
        return this;
    }

    @Nullable
    public TwitchGameInfo getGameInfo(String gameName){
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.twitch.tv/helix/games?name="+gameName)
                    .get()
                    .addHeader("Authorization", "Bearer " + APP_ACCESS_TOKEN)
                    .addHeader("Client-ID", CLIENT_ID)
                    .build();
            Response response = client.newCall(request).execute();
            JsonObject object = new Gson().fromJson(response.body().string(), JsonObject.class);
            response.close();
            if(object.getAsJsonArray("data").size() > 0){
                return new TwitchGameInfo(object.getAsJsonArray("data").get(0).getAsJsonObject());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void send_patch(){
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody payload = RequestBody.create(body.toString(), mediaType);
            Request request = new Request.Builder()
                    .url("https://api.twitch.tv/helix/channels?broadcaster_id=" + BROADCASTER_ID)
                    .patch(payload)
                    .addHeader("Authorization", "Bearer " + USER_ACCESS_TOKEN)
                    .addHeader("Client-ID", CLIENT_ID)
                    .build();

            client.newCall(request).execute().close();;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}