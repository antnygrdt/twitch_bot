package fr.tsuyo.twitchbot.command;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TwitchCommand {

    private final File file = new File("cmds.json");

    private String message;
    private int cooldown;
    private Long lastUse;

    private final String name;
    public TwitchCommand(String name) {
        this.name = name;

        if(exists()){
            JsonObject obj = get().getAsJsonObject(name);
            message = obj.get("message").getAsString();
            cooldown = obj.get("cooldown").getAsInt();
            lastUse = obj.get("lastUse").getAsLong();
        }
    }

    public void update(){
        JsonObject obj = get();
        if(!exists()) obj.add(name, new JsonObject());

        JsonObject cmdObj = obj.getAsJsonObject(name);
        cmdObj.addProperty("message", message);
        cmdObj.addProperty("cooldown", cooldown);
        cmdObj.addProperty("lastUse", System.currentTimeMillis());

        save(obj);
    }

    public void delete(){
        JsonObject obj = get();
        if(exists()) obj.remove(name);

        save(obj);
    }

    public boolean exists(){
        return get().has(name);
    }

    private JsonObject get(){
        JsonParser jsonParser = new JsonParser();
        try (FileReader reader = new FileReader(file)) {
            return (JsonObject) jsonParser.parse(reader);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JsonObject();
    }

    private void save(JsonObject object){
        try (FileWriter f = new FileWriter(file)) {
            f.write(new GsonBuilder().setPrettyPrinting().create().toJson(object));
            f.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public String getMessage(){
        return message;
    }

    public int getCooldown(){
        return cooldown;
    }

    public Long getLastUse(){
        return lastUse;
    }
}