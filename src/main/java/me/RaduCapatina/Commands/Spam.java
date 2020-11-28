package Commands;

import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class Spam extends ListenerAdapter {

    public static void sendPrivateMessage(@NotNull User user, final String content) {
        // openPrivateChannel provides a RestAction<PrivateChannel>
        // which means it supplies you with the resulting channel
        user.openPrivateChannel().queue(new Consumer<PrivateChannel>() {
            public void accept(PrivateChannel channel) {
                channel.sendMessage(content).queue();
            }
        });
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        final String[] message = event.getMessage().getContentRaw().split(" ");

        if (message[0].equalsIgnoreCase("~spam")) {
            for (int i = 1; i <= Integer.valueOf(message[2]); i++)
                sendPrivateMessage(event.getMessage().getMentionedUsers().get(0), message[1].toUpperCase());
        }
    }
}