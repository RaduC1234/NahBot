package me.RaduCapatina.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.ShardCacheView;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.image.SampleModel;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static me.RaduCapatina.Commands.au.Initializer.players;

public class AmongUs extends Command {
    public boolean onGame = false;
    protected String[] emotes = {":nah_au_yellow:777956410524827680", ":nah_au_white:777956410167132171",
            ":nah_au_red:777956410096615435", ":nah_au_purple:777956410306330624", ":nah_au_pink:777956410537279528",
            ":nah_au_orange:777956410393755648", ":nah_au_green:777956410373570562", ":nah_au_cyan:777956410520109117",
            ":nah_au_brown:777956410528759848", ":nah_au_blue:777956409996214293", ":nah_au__black:777956410508050442"};
    private EventWaiter waiter;

    public AmongUs(EventWaiter waiter) {
        this.waiter = waiter;
        this.name = "au";
        this.aliases = new String[]{"hi"};
        this.arguments = "<item> <item>";
        this.help = "says hello and waits for a response";
    }

    @Override
    protected void execute(final CommandEvent event) {

        if (!event.getAuthor().isBot()) {
            if (!event.getArgs().isEmpty()) {
                String[] args = event.getArgs().split("\\s+");
                // if (event.getMember().getVoiceState().inVoiceChannel()) {
                if (!onGame) {
                    if (args[0].length() == 6) {
                        onGame = true;
                        Vector<Member> gameMembers = new Vector<Member>();
                        final EmbedBuilder initializer = new EmbedBuilder()
                                .setTitle("Lobby is Open!")
                                .setDescription("\n ❌**Game not started yet! Enter code `" + args[0].toUpperCase() + "` to join the game!**❌\n")
                                .setColor(new Color(0x0B9E24))
                                .setFooter("React to this message with your-in game color (or ❌ to leave)", null)
                                .addField("Room Code", args[0].toUpperCase(), true)
                                .addField("Region", getRegion(args[1]), true)
                                .addField("Voice Channel", getChannelName(event), true)
                                .addField("Number of players", Integer.toString(players), true);
                        event.getChannel().sendMessage(initializer.build()).queue(new Consumer<Message>() {
                            public void accept(final Message m) {
                                addReactions(m);
                                Member[] lobbyMembers = new Member[12];
                                final EmbedBuilder embedBuilderList = initializer;

                                waiter.waitForEvent(MessageReactionAddEvent.class,
                                        (e) -> {
                                                return e.getMessageId().equals(m.getId());
                                        },
                                        (e) -> {
                                                e.getChannel().sendMessage("dsadsadsad").queue();
                                        },
                                        1, TimeUnit.MINUTES, new Runnable() {
                                            public void run() {
                                                embedBuilderList
                                                        .clearFields()
                                                        .setTitle("Lobby is Closed!")
                                                        .setColor(new Color(0x030000))
                                                        .setFooter("❌**Lobby failed to start due to inactivity**❌")
                                                        .setDescription("Use `~au [code] [region{eu,na,as}]` to start a new game");
                                                m.clearReactions().complete();
                                                m.editMessage(embedBuilderList.build()).complete();
                                                onGame = false;
                                            }
                                        }

                                );
                                waiter.waitForEvent(MessageReactionAddEvent.class,
                                        new Predicate<MessageReactionAddEvent>() {
                                            public boolean test(MessageReactionAddEvent messageReactionE) {
                                                return messageReactionE.getMessageId().equalsIgnoreCase(m.getId())
                                                        && !hasNoReaction(messageReactionE.getMember(), m, messageReactionE);
                                            }
                                        },
                                        new Consumer<MessageReactionAddEvent>() {
                                            public void accept(MessageReactionAddEvent messageReactionAddEvent) {

                                            }
                                        }
                                );
                            }
                        });
                    } else
                        event.getChannel().sendMessage(event.getAuthor().getAsMention() + " invalid Room Code").queue();
                } else
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " a already in progress. Use `~~end` to end the game.").queue();
                //} else
                //  event.getChannel().sendMessage(event.getAuthor().getAsMention() + " please join a voice channel to start a game.").queue();
            } else
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " no arguments passed to command").queue();
        }
    }

    private boolean hasNoReaction(Member member, Message message, MessageReactionAddEvent event) {
        for (MessageReaction reactions : message.getReactions()) {
            for (User user : message.retrieveReactionUsers(reactions.getReactionEmote().getEmote())) {
                if (event.getUser().equals(user))
                    return false;
            }
        }
        return true;
    }

    private void addReactions(Message m) {
        for (String i : emotes) {
            m.addReaction(i).queue();
        }
        m.addReaction("❌").queue();
    }

    private String getRegion(String a) {
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

    private String getChannelName(CommandEvent event) {
        try {
            return event.getMember().getVoiceState().getChannel().getName();
        } catch (Exception e) {
            return "Unknown";
        }
    }
}
