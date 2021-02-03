package me.RaduCapatina.Setup;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SetupOOBE2 extends Command {

    private final EventWaiter eventWaiter;

    public SetupOOBE2(EventWaiter eventWaiter) {
        this.eventWaiter = eventWaiter;
        this.name = "setup";
    }

    private static void runOOBE1(Message message, CommandEvent event) {

        JSONManager.JSONChannel jsonChannel = JSONManager.returnVoiceChannel(message.getGuild().getId());

        if (!message.getGuild().getMember(message.getGuild().getSelfMember().getUser()).getPermissions().contains(Permission.MANAGE_CHANNEL) ||
                !message.getGuild().getMember(message.getGuild().getSelfMember().getUser()).getPermissions().contains(Permission.ADMINISTRATOR)) {
            message.delete().queue();
            message.getChannel().sendMessage("Error: Cannot create a new voice channel. Reasons: Insufficient Permissions").queue();
        }

        try {
            if (!jsonChannel.getCategoryId().equals("000000000000000000"))
                message.getGuild().getCategoryById(jsonChannel.getCategoryId()).delete().queue();

            if (!jsonChannel.getChannelId().equals("000000000000000000"))
                message.getGuild().getVoiceChannelById(jsonChannel.getCategoryId()).delete().queue();
        }
        catch (NullPointerException ignored) {
            //do nothing
        }

        //String categoryId = message.getGuild().createCategory("------\uD83D\uDD0AVoice Rooms\uD83D\uDD0A------").complete().getId();
        //String channelId = message.getGuild().getCategoryById(categoryId).createVoiceChannel("➕Click to create VC").complete().getId();

        //JSONManager.setupAutoVoiceChannel(message.getGuild().getId(), new JSONManager.JSONChannel(categoryId, channelId));

        EmbedBuilder setup = new EmbedBuilder()
                .setColor(new Color(0x01DC38))
                .setAuthor("AutoVoiceChannel Setup.")
                .setThumbnail("https://image.freepik.com/free-vector/illustration-gear-doodle-icon_53876-5596.jpg")
                .addField("Congrats, you set up auto-voice channel.", "Write `~~help` for a full list of commands.\n" +
                        "If something goes wrong you can run this setup again", false);
        message.editMessage(setup.build()).queue();

        message.removeReaction("✅").queue();
        message.removeReaction("✅",event.getAuthor()).queue();
    }

    @Override
    protected void execute(final CommandEvent event) {

        final TextChannel main = event.getGuild().getDefaultChannel();
        final EmbedBuilder setup = new EmbedBuilder()
                .setColor(new Color(0x01DC38))
                .setDescription("This will guild you step by step for setting NahBot to a full extend.")
                .setThumbnail("https://image.freepik.com/free-vector/illustration-gear-doodle-icon_53876-5596.jpg")
                .setAuthor("Welcome to NahBot setup menu", null)
                .addField("Features Covered in this setup",
                        "`AutoVoiceChannel` - creates a voice channel on demand\n" +
                                "`Moderators System` - simplifies the moderators for everyone\n" +
                                "`AutoMatchMaking` - basic voice matchmaking for discord", false);

        final Message message = event.getChannel().sendMessage(setup.build()).complete();

        ButtonMenu.Builder builder = new ButtonMenu.Builder();
        builder.addChoice("✅");
        builder.setAction(new Consumer<MessageReaction.ReactionEmote>() {
            public void accept(MessageReaction.ReactionEmote reactionEmote) {
                runOOBE1(message,event);
            }
        });
        builder.setTimeout(1, TimeUnit.MINUTES);
        builder.setText("\u200E");
        builder.setEventWaiter(eventWaiter);
        builder.build().display(message);
    }
}
