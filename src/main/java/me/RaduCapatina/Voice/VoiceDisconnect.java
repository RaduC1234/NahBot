package me.RaduCapatina.Voice;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class VoiceDisconnect extends Command {

    public VoiceDisconnect(){
        this.guildOnly = true;
        this.name = "disconnect";
        this.aliases = new String[] {"l", "leave"};
    }

    @Override
    protected void execute(CommandEvent event) {
        if(!event.getAuthor().isBot()){
            VoiceChannel connectedChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();
            if(connectedChannel == null) {
                event.getChannel().sendMessage("I am not connected to a voice channel!").queue();
                return;
            }
            // Disconnect from the channel.
            event.getGuild().getAudioManager().closeAudioConnection();
        }

    }

}
