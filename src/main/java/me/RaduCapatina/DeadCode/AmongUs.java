package me.RaduCapatina.DeadCode;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class AmongUs extends ListenerAdapter {

    /*<:nah_au_darkgreen:777956410549731400> <:nah_au_yellow:777956410524827680>
      <:nah_au_white:777956410167132171> <:nah_au_red:777956410096615435>
      <:nah_au_purple:777956410306330624> <:nah_au_pink:777956410537279528>
      <:nah_au_orange:777956410393755648> <:nah_au_green:777956410373570562>
      <:nah_au_cyan:777956410520109117> <:nah_au_brown:777956410528759848>
      <:nah_au_blue:777956409996214293> <:nah_au__black:777956410508050442>
      */
    public boolean onGame = false;

    private final EventWaiter eventWaiter;
    private final String[] emojyID = {"777956410549731400", "777956410524827680", "777956410167132171", "777956410096615435", "777956410306330624",
            "777956410537279528", "777956410393755648", "777956410373570562", "777956410520109117", "777956410528759848", "777956409996214293", "777956410508050442"};
    private String auPrefix = "au";
    private int players  = 0;
    private MessageEmbed initializer;
    private Message start;

    public AmongUs (EventWaiter eventWaiter){
        this.eventWaiter = eventWaiter;
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            final String[] message = event.getMessage().getContentRaw().split(" ");
            //au start [code(6)] [region(2){eu,na,as}]
            if (message[0].equalsIgnoreCase(auPrefix + "start")) {
                if (event.getMember().getVoiceState().inVoiceChannel()) {
                    if (!onGame) {
                        if (message[1].length() == 6) {
                            onGame = true;
                            initializer = new EmbedBuilder()
                                    .setTitle("Lobby is Open!")
                                    .setDescription("\n ❌**Game not started yet! Enter code `" + message[1].toUpperCase() + "` to join the game!**❌\n")
                                    .setColor(new Color(0x0B9E24))
                                    .setFooter("React to this message with your-in game color(or ❌ to leave)", null)
                                    .addField("Room Code", message[1].toUpperCase(), true)
                                    .addField("Region", getRegion(message[2]), true)
                                    .addField("Voice Channel", getChannelName(event), true)
                                    .addField("Number of players", Integer.toString(players), true)
                                    .build();

                            start = event.getChannel().sendMessage(initializer).complete();

                            ButtonMenu.Builder startBuilder = new ButtonMenu.Builder();

                                    startBuilder.addChoice(":nah_au_darkgreen:777956410549731400");
                                    startBuilder.setAction((reactionEmote) -> {
                                        System.out.println("eaewe");
                                    });
//                                    startBuilder.addChoice(":nah_au_yellow:777956410524827680");
//                                    startBuilder.setAction(new Consumer<MessageReaction.ReactionEmote>() {
//                                        public void accept(MessageReaction.ReactionEmote reactionEmote) {
//                                            System.out.println("yellow");
//                                        }
//                                    });
                                    startBuilder.setEventWaiter(eventWaiter);
                                    startBuilder.setTimeout(1, TimeUnit.MINUTES);
                                    startBuilder.setText("\u200E");
                                    startBuilder.build().display(start);

                        } else
                            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " invalid Room Code").queue();
                    } else
                        event.getChannel().sendMessage(event.getAuthor().getAsMention() + " a game has already started. Use `~~end` to end the game.").queue();
                } else
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " please join a voice channel to start a game.").queue();
            } else if (message[0].equalsIgnoreCase(auPrefix + "end")) {
                if (onGame) {
                    onGame = false;
                    // end code here
                        initializer = new EmbedBuilder()
                                .setTitle("Looby Closed")
                                .setDescription("\n ❌**Looby Closed!**")
                                .setColor(new Color(0xD20101))
                                .build();
                    start.editMessage(initializer).queue();

                } else
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " no game is in progress. Join a voice channel\n and use `au start [Room Code] [Region(EU,US,AS)]` to start a game").queue();
            }
        }
    }
    private static String getRegion(String a) {
        String[] regions = {"EU", "NA", "AS"};
        String region;
        if ("EU".equalsIgnoreCase(a)) {
            region = "Europe";
        } else if ("NA".equalsIgnoreCase(a)) {
            region = "North America";
        } else if ("AS".equalsIgnoreCase(a)) {
            region = "Asia";
        } else {
            region = "UNKNOWN";
        }
        return region;
    }

    private static String getChannelName(GuildMessageReceivedEvent event) {
        try {
            return event.getMember().getVoiceState().getChannel().getName();
        } catch (Exception e) {
            return "Unknown";
        }
    }
    public void onEmoteAdded(GuildMessageReactionAddEvent event, Message message){
        if(event.getMessageId().equals(message.getId())){

        }
    }
}
