package fr.tsuyo.twitchbot.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.tsuyo.twitchbot.Main;
import fr.tsuyo.twitchbot.command.commands.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {

    private final List<ICommand> commands = new ArrayList<>();
    private final String prefix = Main.instance.PREFIX;

    public CommandManager(){
        addCommand(new CmdCommand());
        addCommand(new RLCommand());
        addCommand(new VocCommand());
        addCommand(new TitleCommand());
        addCommand(new GameCommand());
        addCommand(new SubOnCommand());
        addCommand(new SubOffCommand());


        File file = new File("cmds.json");
        if(!file.exists()){
            try (FileWriter f = new FileWriter(file)) {
                file.createNewFile();
                f.write(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonObject()));
                f.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addCommand(ICommand cmd){
        boolean nameFound = commands.stream().anyMatch((it) -> it.name().equalsIgnoreCase(cmd.name()));
        if(nameFound){
            throw new IllegalArgumentException("Une commande à ce nom existe déjà !");
        }
        commands.add(cmd);
    }

    private ICommand getCommand(String command){
        command = command.toLowerCase();
        for (ICommand cmd : this.commands) {
            if(cmd.name().equals(command) || cmd.aliases().contains(command)) return cmd;
        }
        return null;
    }

    public void handle(ChannelMessageEvent event, String[] args){
        String[] split = event.getMessage()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");
        String invoke = split[0].toLowerCase();

        try{
            ICommand cmd = this.getCommand(invoke);
            cmd.run(event, args);
        }catch (NullPointerException e){
            TwitchCommand command = new TwitchCommand(invoke);
            if(command.exists()){
                Long currentTime = System.currentTimeMillis();
                Long lastUse = command.getLastUse();
                String message = command.getMessage();
                int cooldown = command.getCooldown();

                if((currentTime - lastUse) > cooldown * 1000L) {
                    command.update();
                    String icon = cooldown == 0 ? "" : " [📛" + cooldown + "s]";
                    event.getTwitchChat().sendMessage(event.getChannel().getName(), message + icon);
                }
            }
        }
    }
}