package me.RaduCapatina;

import me.RaduCapatina.Bot.Bot;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Haha extends ListenerAdapter {

    String messageID;

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");

        if (message[0].equalsIgnoreCase(Bot.prefix + "haha")) {
            try {
                writeId(event.getChannel().sendMessage(returnMessage()).complete().getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
            event.getMessage().delete().complete();
        }

        /*if(message[0].equalsIgnoreCase(Bot.prefix + "changeName")){
            event.getMessage().delete().complete();
            Member member = event.getMessage().getMentionedMembers().get(0);

            member.modifyNickname(message[1]).complete();
        }*/
    }

    @Override
    public void onGuildMessageDelete(@Nonnull GuildMessageDeleteEvent event) {
        try {
            if (event.getMessageId().equalsIgnoreCase(readId())) {
                try {
                    writeId(event.getChannel().sendMessage(returnMessage()).complete().getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static String returnMessage(){
        return "<@706238634923261975> cine schimba numele la boti sau lui <@299974063432925185> e gay. \n" +
                "**Nu sterge mesajul asta sau o sa enervezi pe toata lumea .**\n" +
                "Actiunea este automata deci o faci pe propria raspundere.";
    }
    private void writeId(String Id) throws IOException {
        FileWriter file = new FileWriter(".\\src\\main\\externalFiles\\haha.txt");
        file.write(Id);
        file.close();
    }
    private String readId() throws FileNotFoundException {
        File file = new File(".\\src\\main\\externalFiles\\haha.txt");
        Scanner tokenScanner = new Scanner(file);
        return tokenScanner.nextLine();
    }

}
