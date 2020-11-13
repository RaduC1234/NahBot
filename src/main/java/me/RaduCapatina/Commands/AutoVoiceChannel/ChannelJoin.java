package me.RaduCapatina.Commands.AutoVoiceChannel;

import me.RaduCapatina.Bot.Bot;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ChannelJoin extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        if(event.getChannelJoined().equals(event.getGuild().getVoiceChannelById("775303812914741268"))) {
            Member member = event.getChannelJoined().getMembers().get(0);
            String name = "\uD83D\uDD13 " + member.getEffectiveName() + "'s VC";
            Bot.voiceAdminList.add(member);
            VoiceChannel vc = event.getGuild().createVoiceChannel(name,event.getGuild().getCategoryById("717752672018628658")).setUserlimit(10).complete();
            event.getGuild().moveVoiceMember(event.getMember(),vc).queue();
        }
    }
}
/*guild.createVoiceChannel(name,category)
        //.addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL), null)
        //.addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
        .queue(); // this actually sends the request to discord.    */