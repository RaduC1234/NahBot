package me.RaduCapatina.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;

public class Help extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        if(message[0].equals("~~help"))
        {
            EmbedBuilder help = new EmbedBuilder()
                    .setDescription("My prefix is `~~`")
                    .setColor(new Color(0x01DC38))
                    //.setTimestamp(formatter.format(date))
                    .setFooter("Requested by " + event.getMember().getEffectiveName(), event.getAuthor().getAvatarUrl())
                    .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                    .setAuthor("NahBot Commands", null, event.getAuthor().getAvatarUrl())
                    .addField("Generic Commands", "`help` - displays this message \n`au help` - displays Among Us commnads", false)
                    .addField("Channel Commands", "`kick [member] {reason}` " + "- kicks the mentioned member in your vc\n" +
                            "`ban  [member] {reason}`  -bans the mentioned member in your vc\n" +
                            "`pardon [member] {reason}`  -unbans the mentioned member in your vc\n" +
                            "`lock` - locks the channel you are in in your vc\n" +
                            "`unlock` - unlocks the channel you are in in your vc\n", false);
            event.getChannel().sendMessage(help.build()).queue();
        }
    }
}
