package fr.tsuyo.twitchbot.command.commands;

import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import fr.tsuyo.twitchbot.Main;
import fr.tsuyo.twitchbot.command.ICommand;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public class VocCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")

    private final Dotenv config = Main.instance.getConfig();

    @Override
    public void run(ChannelMessageEvent event, String[] args) {
        TwitchChat chat = event.getTwitchChat();
        String channel = event.getChannel().getName();

        Guild guild = Main.instance.getShardManager().getGuildById(config.get("DISCORD_GUILD"));
        Member member = guild.getMemberById(config.get("DISCORD_MEMBER_ID"));
        GuildVoiceState guildVoiceState = member.getVoiceState();
        if(guildVoiceState.inAudioChannel()){
            AudioChannel audioChannel = guildVoiceState.getChannel();
            if(audioChannel.getMembers().size() < 2){
                chat.sendMessage(channel, "Personne en vocal");
            }else{
                List<Member> list = audioChannel.getMembers();
                StringBuilder members = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    members.append(list.get(i).getUser().getName());
                    if(i+1 != list.size()) members.append(", ");
                }

                chat.sendMessage(channel, "Liste des personnes présentes dans le vocal : " + members + ".");
            }
        }else{
            chat.sendMessage(channel, "Pas en voc");
        }
    }

    @Override
    public String name() {
        return "voc";
    }
}
