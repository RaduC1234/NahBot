package me.RaduCapatina.Voice;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.managers.AudioManager;

public class VoiceConnect extends Command {

    public VoiceConnect(){
        this.guildOnly = true;
        this.name = "connect";
        this.aliases = new String[] {"p", "play"};
        this.arguments = "<args>";
    }

    @Override
    protected void execute(final CommandEvent event) {
        if(!event.getAuthor().isBot()){
            if(!event.getGuild().getSelfMember().hasPermission(Permission.VOICE_CONNECT)){
                // The bot does not have permission to join any voice channel.
                event.getChannel().sendMessage("I do not have permissions to join a voice channel!").queue();
                return;
            }
            AudioManager audioManager = event.getGuild().getAudioManager();
            audioManager.openAudioConnection(event.getMember().getVoiceState().getChannel());
        }
    }
}
