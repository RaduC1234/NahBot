package me.RaduCapatina.Bot;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class Shutdown extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        if(message[0].equals("~~shutdown") && event.getAuthor().getId().equals("318102952168390656"))
        {
          event.getMessage().addReaction("\uD83D\uDC98").queue();
          System.out.println("Shutdown");
          event.getJDA().shutdown();
        }
    }
}
