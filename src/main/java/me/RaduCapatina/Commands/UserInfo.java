package me.RaduCapatina.Commands;

import me.RaduCapatina.Bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.print.DocFlavor;
import java.awt.*;

public class UserInfo extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");

        if(!event.getAuthor().isBot() && message[0].equalsIgnoreCase(Bot.prefix + "user-info")) {
            if(message.length == 2)
            {
                Member mentionedMember = event.getMessage().getMentionedMembers().get(0);
                EmbedBuilder userInfo = new EmbedBuilder()
                        .setColor(new Color(121912))
                        .setFooter("Requested by #" + event.getAuthor().getName() + " | " + event.getAuthor().getId())
                        .setThumbnail(mentionedMember.getUser().getEffectiveAvatarUrl())
                        .addField(" ‎‏‏‎ ‎\u200F\u200F\u200E \u200E\u200F\u200F\u200E" +
                                " \u200E\u200F\u200F\u200E \u200E\u200F\u200F\u200E " +
                                "\u200E\u200F\u200F\u200E \u200E\u200F\u200F\u200E " +
                                "\u200E\u200F\u200F\u200E \u200E\u200F\u200F\u200E " +
                                "\u200E\u200F\u200F\u200E \u200E\u200F\u200F\u200E " +
                                "\u200E\u200F\u200F\u200E \u200E\u200F\u200F\u200E " +
                                "\u200E\u200F\u200F\u200E \u200E\u200F\u200F\u200E " +
                                "\u200E\u200F\u200F\u200E\u200F\u200F\u200E  \u200E\u200F\u200F\u200E" +
                                " \u200E\u200F\u200F\u200E \u200E\u200F\u200F\u200E \u200E\u200F\u200F\u200E" +
                                "General Information", "**Name** " + mentionedMember.getEffectiveName() + "\n**Tag** #" + mentionedMember.getUser().getDiscriminator(), true)
                        .addField("‏‏‎ ‎", "**Nickname** " + getNickname(mentionedMember) + "\n**UserId** " + mentionedMember.getUser().getId(), true)
                        .addBlankField(false)
                        .addField("Joined Server ", getTimeRow1(mentionedMember), true)
                        .addField("Joined Discord",getTimeRow2(mentionedMember), true)
                        .addField("Role Status", getRoleStatus(mentionedMember), true);
                event.getChannel().sendMessage(userInfo.build()).queue();
            }
            else event.getChannel().sendMessage(event.getAuthor().getAsMention() + " please mention someone").queue();
        }
    }
    private String getNickname(Member member){
        if(member.getNickname() == null)
            return member.getEffectiveName();
        return member.getNickname();
    }
    private String getTimeRow1(Member member){
        String month = member.getTimeJoined().getMonth().name().toLowerCase();
        String day =  Integer.toString(member.getTimeJoined().getDayOfMonth());
        String year = Integer.toString(member.getTimeJoined().getYear());
        String hour = Integer.toString(member.getTimeJoined().getHour());
        String minute = Integer.toString(member.getTimeJoined().getMinute());

        return day + "-" + month + "-" + year + "\n" + hour + ":" + minute;
    }
    private String getTimeRow2(Member member){
        String month = member.getTimeCreated().getMonth().name().toLowerCase();
        String day =  Integer.toString(member.getTimeCreated().getDayOfMonth());
        String year = Integer.toString(member.getTimeCreated().getYear());
        String hour = Integer.toString(member.getTimeCreated().getHour());
        String minute = Integer.toString(member.getTimeCreated().getMinute());

        return day + "-" + month + "-" + year + "\n" + hour + ":" + minute;
    }
    private String getRoleStatus(Member member){
        if(member.getPermissions().contains(Permission.ADMINISTRATOR))
            return "Server Administrator";
        return "Server Member";
    }
}
