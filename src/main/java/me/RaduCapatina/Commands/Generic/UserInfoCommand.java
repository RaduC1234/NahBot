package me.RaduCapatina.Commands.Generic;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class UserInfoCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String message[] = event.getMessage().getContentRaw().split(" ");
        User user1 = event.getMessage().getMentionedUsers().get(0);
        if (message[0].equals("~user-info") && message.length == 2) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("User information for: " + user1.getName() + "#" + user1.getDiscriminator());
            eb.setColor(Color.RED);
            eb.addField("User information for: " + user1.getName() + "#" + user1.getDiscriminator(), " ", true);
            eb.setThumbnail(user1.getEffectiveAvatarUrl());
            event.getChannel().sendMessage(eb.build()).queue();
        }
    }
}
