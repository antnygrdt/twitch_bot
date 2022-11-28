package fr.tsuyo.twitchbot.command.commands;

import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.enums.CommandPermission;
import fr.tsuyo.twitchbot.Main;
import fr.tsuyo.twitchbot.command.ICommand;
import fr.tsuyo.twitchbot.command.TwitchCommand;

public class CmdCommand implements ICommand {

    private final String prefix = Main.instance.PREFIX;

    @Override
    public void run(ChannelMessageEvent event, String[] args) {
        TwitchChat chat = event.getTwitchChat();
        String channel = event.getChannel().getName();

        if(!(event.getPermissions().contains(CommandPermission.MODERATOR) || event.getPermissions().contains(CommandPermission.BROADCASTER))) return;
        if(args.length < 2 || !(args[0].equals("add") || args[0].equals("remove") || args[0].equals("delete") || args[0].equals("del"))) {
            chat.sendMessage(channel, "Erreur lors de la saisie de la commande."); return; }

        String cmd;
        cmd = args[1];
        if(cmd.split("")[0].equals(prefix)) cmd = cmd.substring(1);

        TwitchCommand command = new TwitchCommand(cmd);
        switch (args[0]){
            case "add":
                if(args.length < 3) { chat.sendMessage(channel, "Veuillez saisir un texte @" + event.getUser().getName()+"."); return; }

                StringBuilder message = new StringBuilder();
                int cooldown = 0;
                for (int i = 0; i < args.length; i++) {
                    String completion = message.toString().equals("") ? "" : " ";
                    if(i > 1){
                        if(!args[i].contains("$cmd")){
                            message.append(completion).append(args[i]);
                        }else{
                            String[] arguments = args[i].replace("$cmd(", "").replace(")", "").split(",");
                            if(arguments[0].equalsIgnoreCase("cooldown")){
                                try{
                                    int cd = Integer.parseInt(arguments[1]);
                                    if (cd < 0) {
                                        throw new NumberFormatException();
                                    }else{ cooldown = cd; }
                                } catch (NullPointerException | NumberFormatException ignored){
                                    chat.sendMessage(channel, "Erreur lors de la saisie de la commande."); return;
                                }
                            }else{
                                chat.sendMessage(channel, "Erreur lors de la saisie de la commande."); return;
                            }
                        }
                    }
                }

                command.setMessage(message.toString());
                command.setCooldown(cooldown);
                command.update();
                chat.sendMessage(channel, "/me La commande '" + cmd + "' vient d'être crée par @"+event.getUser().getName()+".");
                break;

            case "remove":
            case "delete":
            case "del":
                if(args.length != 2) { chat.sendMessage(channel, "Erreur lors de la saisie de la commande."); return; }
                if(command.exists()){
                    command.delete();
                    chat.sendMessage(channel, "La commande '" + cmd + "' a été supprimé.");
                }else{
                    chat.sendMessage(channel, "La commande '" + cmd + " n'existe pas.");
                }

                break;
        }
    }

    @Override
    public String name() {
        return "cmd";
    }
}
