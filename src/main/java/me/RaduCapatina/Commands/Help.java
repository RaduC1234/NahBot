package me.RaduCapatina.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.function.Consumer;

public class Help extends ListenerAdapter {
    
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        if(message[0].equals("~~help"))
        {
            event.getChannel().sendMessage(returnEmbed(event.getAuthor(), event.getJDA()).build()).queue();
        }
    }
    @Override
    public void onPrivateMessageReceived(@Nonnull final PrivateMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        if(message[0].equals("~~help"))
        {
            event.getAuthor().openPrivateChannel().queue(new Consumer<PrivateChannel>() {
                public void accept(PrivateChannel channel) {
                    channel.sendMessage(returnEmbed(event.getAuthor(), event.getJDA()).build()).queue();
                }
            });
        }
    }
    private EmbedBuilder returnEmbed(User user, JDA jda) {
        return new EmbedBuilder()
                .setDescription("My prefix is `~~`")
                .setColor(new Color(0x01DC38))
                //.setTimestamp(formatter.format(date))
                .setFooter("Requested by " + user.getName() + "#" +user.getDiscriminator() + " | " + user.getId(),
                        user.getAvatarUrl())
                .setThumbnail(jda.getSelfUser().getAvatarUrl())
                .setAuthor("NahBot Commands", null, user.getAvatarUrl())
                .addField("Generic Commands", "`help` - displays this message \n`au help` - displays Among Us commnads", false)
                .addField("Channel Commands", "`kick [member] {reason}` " + "- kicks the mentioned member in your vc\n" +
                        "`ban  [member] {reason}`  -bans the mentioned member in your vc\n" +
                        "`pardon [member] {reason}`  -unbans the mentioned member in your vc\n" +
                        "`lock` - locks the channel you are in in your vc\n" +
                        "`unlock` - unlocks the channel you are in in your vc\n", false);
    }
}
