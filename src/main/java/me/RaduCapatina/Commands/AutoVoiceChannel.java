package me.RaduCapatina.Commands;

import me.RaduCapatina.Bot.Bot;
import me.RaduCapatina.Setup.JSONSetup;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class AutoVoiceChannel extends ListenerAdapter {



    /*      This bot has a system that auto-generates voice channels.
     *
     *      It works by checking if someone has joined the special channel, them it creates another voice channel and
     * then it moves that user there. It also makes that User an "voiceAdmin" defined in voiceAdminList Vector.
     *
     *      If an admin Left the channel, the system picks an random user.
     *      If the channel is empty, it will be deleted.
     *
     *      Commands: ~~kick (kicks the User without been able to rejoin). Aliases: ~~ ban
     *                ~~pardon (unbans the User from the voice channel
     *                ~~lock (locks the channel)
     *                ~~unlock (unlocks the channel)
     *
     *      Special prefix: "~~"
     *
     *      If you want to use it for your own server be sure to replace the "Create Voice channel" channel
     *  and the category that contains that channel.
     */

    private static String returnChannelId(GenericGuildVoiceEvent event) { // returns the channelID from guild.json
        JSONSetup.JSONChannel id = JSONSetup.returnVoiceChannel(event.getGuild().getId());
        return id.getChannelId();
    }

    private static String returnCategoryId(GenericGuildVoiceEvent event) { // returns the categoryID from guild.json
        JSONSetup.JSONChannel id = JSONSetup.returnVoiceChannel(event.getGuild().getId());
        return id.getCategoryId();
    }

    private void modifyRolesChannelVisibility(GenericGuildMessageEvent event, VoiceChannel voiceChannel, boolean allow) {
        // this modifies the visibility for all guild roles except ones included in guild.json.
        // allow = 1 -> allow to see
        // allow = 0 -> deny to see
        JSONSetup.JsonRoles roles = JSONSetup.returnRoles(event.getGuild().getId());

        if (!allow) {
            try {
                if (!roles.isUsePublicRole()) {
                    voiceChannel.putPermissionOverride(event.getGuild().getRoleById(roles.getAccessRole())).deny(Permission.VOICE_CONNECT).queue();
                }
            } catch (NullPointerException e) {
                event.getChannel().sendMessage("Error: cannot lock your channel because the guild file is absent.\nIf this error persists please contact a moderator");
            }

            voiceChannel.putPermissionOverride(event.getGuild().getPublicRole()).deny(Permission.VOICE_CONNECT).queue();
        } else {
            try {
                if (!roles.isUsePublicRole()) {
                    voiceChannel.putPermissionOverride(event.getGuild().getRoleById(roles.getAccessRole())).setAllow(Permission.VOICE_CONNECT).queue();
                }
            } catch (NullPointerException e) {
                event.getChannel().sendMessage("Error: cannot unlock your channel because the guild file is absent.\nIf this error persists please contact a moderator");
            }
            voiceChannel.putPermissionOverride(event.getGuild().getPublicRole()).setAllow(Permission.VOICE_CONNECT).queue();
        }
    }

    private void onChannelLock(GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equalsIgnoreCase(Bot.prefix + "lock")) {  //TODO: FIX ALREADY LOCKED MESSAGE
            if (Bot.voiceAdminList.contains(event.getMember())) {
                try {
                    VoiceChannel vc = event.getMember().getVoiceState().getChannel();
                    modifyRolesChannelVisibility(event, vc, false);
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
        if (event.getMessage().getContentRaw().equalsIgnoreCase(Bot.prefix + "unlock")) {    //TODO: FIX ALREADY UNLOCKED MESSAGE
            if (Bot.voiceAdminList.contains(event.getMember())) {
                try {
                    VoiceChannel vc = event.getMember().getVoiceState().getChannel();
                    modifyRolesChannelVisibility(event, vc, true);
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

    private void onChannelKick(GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().toLowerCase().startsWith(Bot.prefix + "kick")) {
            Member member = event.getMessage().getMentionedMembers().get(0);
            if (Bot.voiceAdminList.contains(event.getMember())) {
                if (!event.getMember().equals(member)) {
                    try {
                        if (member.getVoiceState().getChannel().equals(event.getMember().getVoiceState().getChannel())) {
                            event.getGuild().kickVoiceMember(member).queue();
                            event.getMember().getVoiceState().getChannel().upsertPermissionOverride(member).deny(Permission.VOICE_CONNECT).queue();
                            event.getMessage().addReaction("✅").queue();
                        } else event.getChannel().sendMessage(event.getMember().getEffectiveName() +
                                " the mentioned user is not in a voice channel with you. \uD83D\uDE03\uD83D\uDD2B").queue(); //face-gun
                    } catch (NullPointerException e) {
                        event.getChannel().sendMessage(event.getMember().getEffectiveName() +
                                " i can't kick someone which is not in a voice channel. \uD83D\uDC80\uD83D\uDC80").queue(); //skull-skull
                    } catch (Exception e) {
                        event.getChannel().sendMessage(event.getMember().getEffectiveName() +
                                " unknown error: " + e.toString() + "Please contact a moderator if this error repeats.").queue(); //skull-skull
                    }
                } else event.getChannel().sendMessage(event.getMember().getEffectiveName() +
                        " u cant't kick yourself.\uD83D\uDE24\uD83D\uDE24").queue();
            } else event.getChannel().sendMessage(event.getMember().getEffectiveName() +
                    " you are not a channel admin so i can't kick " + member.getEffectiveName()).queue();
        }
    }

    private void onChannelPardon(GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().toLowerCase().startsWith(Bot.prefix + "pardon")) {
            Member member = event.getMessage().getMentionedMembers().get(0);
            if (Bot.voiceAdminList.contains(event.getMember())) {
                if (!event.getMember().equals(member)) {
                    try {
                        event.getMember().getVoiceState().getChannel().upsertPermissionOverride(member).setAllow(Permission.VOICE_CONNECT).queue();
                        event.getMessage().addReaction("✅").queue();
                    } catch (NullPointerException e) {
                        event.getChannel().sendMessage(event.getMember().getEffectiveName() +
                                " you are not in a voice channel. \uD83D\uDC80\uD83D\uDC80").queue(); //skull-skull
                    } catch (Exception e) {
                        event.getChannel().sendMessage(event.getMember().getEffectiveName() +
                                " unknown error: " + e.toString() + "Please contact a moderator if this error repeats.").queue(); //skull-skull
                    }
                } else event.getChannel().sendMessage(event.getMember().getEffectiveName() +
                        " u cant't pardon yourself.❌❌").queue();
            } else event.getChannel().sendMessage(event.getMember().getEffectiveName() +
                    " you are not a channel admin so i can't pardon " + member.getEffectiveName()).queue();
        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            onChannelLock(event);
            onChannelUnlock(event);
            onChannelKick(event);
            onChannelPardon(event);
        }
    }

    //Channel Join
    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        if (event.getChannelJoined().equals(event.getGuild().getVoiceChannelById(returnChannelId(event)))) { //if a member joins the special channel
            Member member = event.getChannelJoined().getMembers().get(0);
            String name = "\uD83D\uDD13 " + member.getEffectiveName() + "'s VC";
            VoiceChannel vc = event.getGuild().createVoiceChannel(name, event.getGuild().getCategoryById(returnCategoryId(event))).setUserlimit(10).complete();
            event.getGuild().moveVoiceMember(event.getMember(), vc).queue();
            Bot.voiceAdminList.add(member);
        }
    }

    // Channel Move
    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        if (event.getChannelJoined().equals(event.getGuild().getVoiceChannelById(returnChannelId(event)))) {

            Member member = event.getChannelJoined().getMembers().get(0);
            String name = "\uD83D\uDD13 " + member.getEffectiveName() + "'s VC";
            Bot.voiceAdminList.add(member);
            VoiceChannel vc = event.getGuild().createVoiceChannel(name, event.getGuild().getCategoryById(returnCategoryId(event))).setUserlimit(10).complete();
            event.getGuild().moveVoiceMember(event.getMember(), vc).queue();
        }
        if (event.getChannelLeft().getMembers().toArray().length == 0
                && !event.getChannelLeft().equals(event.getGuild().getVoiceChannelById(returnChannelId(event)))
                && event.getChannelLeft().getParent().equals(event.getGuild().getCategoryById(returnCategoryId(event)))) {
            Guild guild = event.getGuild();
            VoiceChannel vc = event.getChannelLeft();
            guild.getVoiceChannelById(event.getChannelLeft().getId()).delete().reason("No members in channel").queue();
            if (Bot.voiceAdminList.contains(event.getMember()))
                Bot.voiceAdminList.remove(event.getMember());
        }
    }

    //Channel Leave
    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        try
        {
            if (event.getGuild().getCategoryById(returnCategoryId(event)).equals(event.getChannelLeft().getParent())) {

                if (Bot.voiceAdminList.contains(event.getMember())
                        && event.getChannelLeft().getParent().getId().equals(returnCategoryId(event))) {
                    Bot.voiceAdminList.remove(event.getMember());
                }

                if (event.getChannelLeft().getMembers().toArray().length == 0 && !event.getChannelLeft().equals(event.getGuild().getVoiceChannelById(returnChannelId(event)))) {
                    Guild guild = event.getGuild();
                    VoiceChannel vc = event.getChannelLeft();
                    guild.getVoiceChannelById(event.getChannelLeft().getId()).delete().reason("No members in channel").queue();
                }
            }
        }
        catch (NullPointerException ignored)
        {
        }
    }
}