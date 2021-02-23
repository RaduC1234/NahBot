package me.RaduCapatina.DeadCode;

import me.RaduCapatina.Bot.Bot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutionException;

public class Moderation extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot() && event.getMessage().getContentRaw().startsWith(Bot.prefix)) {
            onModeratorBan(event);
            onModeratorKick(event);
            onModeratorMute(event);
        }
    }

    private void onModeratorBan(final GuildMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        if (message[0].equalsIgnoreCase(Bot.prefix + "ban"))
            if (event.getMember().getPermissions().contains(Permission.BAN_MEMBERS)) {
                Member target = event.getMessage().getMentionedMembers().get(0);
                   if(!target.isOwner()){
                   }
                   else {
                       event.getChannel().sendMessage(event.getAuthor().getAsMention() + " sorry but i cannot kick the server owner").queue();
                   }
                }
            else
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " you don't have the permission to ban members").queue();
    }

    private void onModeratorKick(GuildMessageReceivedEvent event) {

    }

    private void onModeratorMute(GuildMessageReceivedEvent event) {

    }
}
