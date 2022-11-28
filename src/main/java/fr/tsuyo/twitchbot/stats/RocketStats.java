package fr.tsuyo.twitchbot.stats;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.tsuyo.twitchbot.Main;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class RocketStats {

    private final JsonObject object;
    public RocketStats() {
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(Main.instance.getConfig().get("RL_TRACKER")).get().build();
            Response response = client.newCall(request).execute();
            this.object = (JsonObject) new JsonParser().parse(response.body().string());
            response.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDisplay(Games game){
        if(object != null){
            for (JsonElement jE : object.getAsJsonObject("data").getAsJsonObject().getAsJsonArray("segments")) {
                String name = jE.getAsJsonObject().getAsJsonObject("metadata").get("name").getAsString();
                if(name.equals(game.jsonName)){
                    JsonObject stats = jE.getAsJsonObject().getAsJsonObject("stats");

                    String tier = stats.getAsJsonObject("tier").getAsJsonObject("metadata").get("name").getAsString();
                    String division = stats.getAsJsonObject("division").getAsJsonObject("metadata").get("name").getAsString();
                    int rating = stats.getAsJsonObject("rating").get("value").getAsInt();

                    return String.format("%s %s (%s)", tier, division, rating);
                }
            }
        }
        return null;
    }

    public enum Games{
        DUEL("Ranked Duel 1v1", "1v1"), DUO("Ranked Doubles 2v2", "2v2"), TRIO("Ranked Standard 3v3", "3v3"),
        HOOPS("Hoops", "basket"), RUMBLE("Rumble", "rumble"), DROPSHOT("Dropshot", "dropshot"),
        SNOWDAY("Snowday", "snowday"), TOURNAMENTS("Tournament Matches", "tournois");

        private final String jsonName;
        private final String name;
        Games(String jsonName, String name){
            this.jsonName = jsonName;
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}