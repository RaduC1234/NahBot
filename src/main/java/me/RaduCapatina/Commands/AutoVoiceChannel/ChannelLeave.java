package me.RaduCapatina.Commands.AutoVoiceChannel;

import me.RaduCapatina.Bot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ChannelLeave extends ListenerAdapter {
    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        if(event.getGuild().getCategoryById("717752672018628658").equals(event.getChannelLeft().getParent())) {

            if(Bot.voiceAdminList.contains(event.getMember())){
                Bot.voiceAdminList.remove(event.getMember());
            }

            if (event.getChannelLeft().getMembers().toArray().length == 0 && !event.getChannelLeft().equals(event.getGuild().getVoiceChannelById("775303812914741268"))) {
                Guild guild = event.getGuild();
                VoiceChannel vc = event.getChannelLeft();
                guild.getVoiceChannelById(event.getChannelLeft().getId()).delete().reason("No members in channel").queue();
            }
        }
    }
}
