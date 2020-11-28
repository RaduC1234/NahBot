package me.RaduCapatina.Commands;

import me.RaduCapatina.Bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class AutoVoiceChannel extends ListenerAdapter {

    private void onChannelLock(GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equalsIgnoreCase(Bot.channelPrefix + "lock")) {
            if (Bot.voiceAdminList.contains(event.getMember())) {
                try {
                    VoiceChannel vc = event.getMember().getVoiceState().getChannel();
                    vc.putPermissionOverride(event.getGuild().getRoleById("769613194879828008")).deny(Permission.VOICE_CONNECT).queue();
                    vc.getManager().setName(event.getMember().getEffectiveName() + "'s VC").queue();
                    event.getMessage().addReaction("✅").queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " Unexpected error: " + e.toString()).queue();
                }
            } else if (event.getMember().getVoiceState().inVoiceChannel())
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " you are not a channel admin.").queue();
            else if (!event.getMember().getVoiceState().inVoiceChannel())
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " you are not in a voice channel").queue();
            else
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " channel is already locked").queue();
        }
    }

    private void onChannelUnlock(GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equalsIgnoreCase(Bot.channelPrefix + "unlock")) {
            if (Bot.voiceAdminList.contains(event.getMember())) {
                try {
                    VoiceChannel vc = event.getMember().getVoiceState().getChannel();
                    vc.putPermissionOverride(event.getGuild().getRoleById("769613194879828008")).setAllow(Permission.VOICE_CONNECT).queue();
                    vc.getManager().setName("\uD83D\uDD13 " + event.getMember().getEffectiveName() + "'s VC").queue();
                    event.getMessage().addReaction("✅").queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " Unexpected error: " + e.toString()).queue();
                }
            } else if (event.getMember().getVoiceState().inVoiceChannel())
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " you are not a channel admin.").queue();
            else if (!event.getMember().getVoiceState().inVoiceChannel())
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " you are not in a voice channel").queue();
            else
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " channel is already unlocked").queue();
        }
    }

    private void onChannelHide(GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equalsIgnoreCase(Bot.channelPrefix + "hide") && Bot.voiceAdminList.contains(event.getMember())) {
            event.getMessage().addReaction("✅").queue();
        }
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        if (event.getGuild().getCategoryById("717752672018628658").equals(event.getChannelLeft().getParent())) {

//            if (Bot.voiceAdminList.contains(event.getMember())) {
//                Bot.voiceAdminList.remove(event.getMember());
//            }

            if (event.getChannelLeft().getMembers().toArray().length == 0 && !event.getChannelLeft().equals(event.getGuild().getVoiceChannelById("775303812914741268"))) {
                Guild guild = event.getGuild();
                VoiceChannel vc = event.getChannelLeft();
                guild.getVoiceChannelById(event.getChannelLeft().getId()).delete().reason("No members in channel").queue();
            }
        }
        if (event.getChannelJoined().equals(event.getGuild().getVoiceChannelById("775303812914741268"))) {
            Member member = event.getChannelJoined().getMembers().get(0);
            String name = "\uD83D\uDD13 " + member.getEffectiveName() + "'s VC";
            Bot.voiceAdminList.add(member);
            VoiceChannel vc = event.getGuild().createVoiceChannel(name, event.getGuild().getCategoryById("717752672018628658")).setUserlimit(10).complete();
            event.getGuild().moveVoiceMember(event.getMember(), vc).queue();
        }

    }

    //Channel Join
    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        if (event.getChannelJoined().equals(event.getGuild().getVoiceChannelById("775303812914741268"))) {
            Member member = event.getChannelJoined().getMembers().get(0);
            String name = "\uD83D\uDD13 " + member.getEffectiveName() + "'s VC";
            Bot.voiceAdminList.add(member);
            VoiceChannel vc = event.getGuild().createVoiceChannel(name, event.getGuild().getCategoryById("717752672018628658")).setUserlimit(10).complete();
            event.getGuild().moveVoiceMember(event.getMember(), vc).queue();
        }
    }

    //Channel Leave
    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        if (event.getGuild().getCategoryById("717752672018628658").equals(event.getChannelLeft().getParent())) {

            if (Bot.voiceAdminList.contains(event.getMember())) {
                Bot.voiceAdminList.remove(event.getMember());
            }

            if (event.getChannelLeft().getMembers().toArray().length == 0 && !event.getChannelLeft().equals(event.getGuild().getVoiceChannelById("775303812914741268"))) {
                Guild guild = event.getGuild();
                VoiceChannel vc = event.getChannelLeft();
                guild.getVoiceChannelById(event.getChannelLeft().getId()).delete().reason("No members in channel").queue();
            }
        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            onChannelLock(event);
            onChannelUnlock(event);
            onChannelKick(event);
            //onChannelPardon(event);
            onChannelHide(event); // just emote
            //TODO: ADD MORE COMMNADS
        }
    }

    private void onChannelKick(GuildMessageReceivedEvent event) {

        if(event.getMessage().getContentRaw().toLowerCase().startsWith(Bot.channelPrefix + "kick")){
            Member member = event.getMessage().getMentionedMembers().get(0);
            if(Bot.voiceAdminList.contains(event.getMember())){
                if(!event.getMember().equals(member)){
                    try{
                        if(member.getVoiceState().getChannel().equals(event.getMember().getVoiceState().getChannel())){
                            event.getGuild().kickVoiceMember(member).queue();
                            event.getMember().getVoiceState().getChannel().upsertPermissionOverride(member).deny(Permission.VOICE_CONNECT).queue();
                            event.getMember().getVoiceState().getChannel().upsertPermissionOverride(member).deny(Permission.VIEW_CHANNEL).queue();
                            event.getMessage().addReaction("✅").queue();
                        }
                        else event.getChannel().sendMessage(event.getMember().getEffectiveName() +
                                " the mentioned user is not in a voice channel with you. \uD83D\uDE03\uD83D\uDD2B").queue(); //face-gun
                    }
                    catch (NullPointerException e){
                        event.getChannel().sendMessage(event.getMember().getEffectiveName() +
                                " i can't kick someone which is not in a voice channel. \uD83D\uDC80\uD83D\uDC80").queue(); //skull-skull
                    }
                    catch (Exception e){
                        event.getChannel().sendMessage(event.getMember().getEffectiveName() +
                                " unknown error: " + e.toString() + "Please contact a moderator if this error repeats.").queue(); //skull-skull
                    }
                }
                else event.getChannel().sendMessage(event.getMember().getEffectiveName() +
                        " u cant't kick yourself.\uD83D\uDE24\uD83D\uDE24" ).queue();
            }
            else event.getChannel().sendMessage(event.getMember().getEffectiveName() +
                    " you are not a channel admin so i can't kick " + member.getEffectiveName()).queue();

        }
    }
}