package me.RaduCapatina.Commands;

import me.RaduCapatina.Bot.Bot;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class Owner extends ListenerAdapter {

    public static void onExeption(GuildMessageReceivedEvent event){

    }
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        if(message[0].equalsIgnoreCase(Bot.prefix + "owner")/*&& event.getGuild().getOwner().equals(event.getAuthor())*/){
            if(message[1].equalsIgnoreCase("setup") /*&& me.RaduCapatina.Bot.Setup == false*/)
            {
                TextChannel welcomeTextChannel = event.getGuild().getTextChannelsByName("\uD83D\uDC4Fwelcome",true).get(0);
                welcomeTextChannel.sendMessage("__**Welcome to " + event.getGuild().getName() + " server!**__ \n \n " +
                         "Want to invite others to this server? Use this link! https://discord.gg/8e7jMrVWej" +
                        "\n \n "
                ).queue();

                welcomeTextChannel.sendMessage("**✅ 1 - Don't be toxic.**\n" +
                        "• We do not want spam, (server) ads, toxicity, harassment, impersonation of other members, etc.\n" +
                        "• Do not try to disturb the peace on this server.\n" +
                        "• If you have a dispute with another member, take it to DMs. Ensure that chat is as inclusive as possible.\n" +
                        "\n" +
                        "**✅ 2 — Your name must be readable, mentionable, and appropriate.**\n" +
                        "• This means that names should not include self promotion, anything that does not follow our other set rules, contain digits only, characters that cannot be easily typed on the US-Intl layout, excessive use of symbols, etc.\n" +
                        "\n" +
                        "**✅ 3 — Do not abuse mentions or spam.**\n" +
                        "• Please avoid using @everyone or @here too often\n" +
                        "• Includes excessive amounts of messages, emojis, capital letters, pings/mentions, etc.\n" +
                        "\n" +
                        "**✅ 4 - No personal information. Protect your privacy and the privacy of others.**\n" +
                        "• Do not distribute your personal information online.\n" +
                        "• This includes: Your real name, your adress, etc.\n" +
                        "\n" +
                        "**✅5 - No piracy, sexual, NSFW, or otherwise suspicious content.**\n" +
                        "• No scam links, URL shorteners, IP grabbers, etc.\n" +
                        "\n" +
                        "**✅6 - Listen to server staff.**\n" +
                        "• If a moderator tells you to stop doing something, stop it\n"

                ).queue();
            }
        }
    }
}
