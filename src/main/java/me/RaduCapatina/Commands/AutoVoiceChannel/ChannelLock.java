package me.RaduCapatina.Commands.AutoVoiceChannel;

import me.RaduCapatina.Bot.Bot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.IPermissionHolder;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.swing.tree.ExpandVetoException;
import java.util.Vector;

public class ChannelLock extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            if (event.getMessage().getContentRaw().equalsIgnoreCase(Bot.channelPrefix + "lock") && Bot.voiceAdminList.contains(event.getMember())) {
                try {
                    VoiceChannel vc = event.getMember().getVoiceState().getChannel();
                    vc.putPermissionOverride(event.getGuild().getRoleById("769613194879828008")).deny(Permission.VOICE_CONNECT).queue();
                    vc.getManager().setName(event.getMember().getEffectiveName() + "'s VC").queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " Unexpected error: " + e.toString()).queue();
                }
            } else if (event.getMessage().getContentRaw().equalsIgnoreCase(Bot.channelPrefix + "lock") && event.getMember().getVoiceState().inVoiceChannel())
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " you are not a channel admin.").queue();
            else if (event.getMessage().getContentRaw().equalsIgnoreCase(Bot.channelPrefix + "lock"))
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " you are not in a voice channel").queue();
            else event.getChannel().sendMessage(event.getAuthor().getAsMention() + " channel is already locked").queue();
        }


    }
}
