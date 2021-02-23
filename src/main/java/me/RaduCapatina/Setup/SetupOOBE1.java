package me.RaduCapatina.Setup;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SetupOOBE1 extends ListenerAdapter {

    @Override
    public void onGuildJoin(@Nonnull final GuildJoinEvent event) {
        TextChannel channel = event.getGuild().getDefaultChannel();
        EmbedBuilder setup = new EmbedBuilder()
                .setDescription("My prefix is `~~`")
                .setColor(new Color(0x01DC38))
                .setFooter("Please run ~~help for a full list of commands")
                .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                .setAuthor("Thanks for adding me", null)
                .addField("Setup", "To have full access to NahBot features please run `setup`", false)
                .addField("Features", "`AutoVoiceChannel` - creates a voice channel on demand", false);
        channel.sendMessage(setup.build()).queue();

    }
}