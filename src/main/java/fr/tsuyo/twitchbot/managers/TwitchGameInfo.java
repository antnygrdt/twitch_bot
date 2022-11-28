package fr.tsuyo.twitchbot.managers;

import com.google.gson.JsonObject;

public class TwitchGameInfo {

    private final String id;
    private final String name;

    public TwitchGameInfo(JsonObject obj) {
        this.id = obj.get("id").getAsString();
        this.name = obj.get("name").getAsString();
    }

    public String getID(){
        return id;
    }

    public String getName(){
        return name;
    }
}