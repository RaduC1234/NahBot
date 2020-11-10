package me.RaduCapatina.Bot;

import me.RaduCapatina.Commands.AutoVoiceChannel.ChannelJoin;
import me.RaduCapatina.Commands.AutoVoiceChannel.ChannelLeave;
import me.RaduCapatina.Commands.Owner;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Bot {

    public static final String prefix = "~";
    public static boolean Setup = false;

    public static void main(String args[]) throws Exception {
        JDA jda = JDABuilder.createDefault(readToken()).build();

        jda.addEventListener(new Owner());
        jda.addEventListener(new ChannelJoin());
        jda.addEventListener(new ChannelLeave());
        setRichPresence(jda);
    }
    private static String readToken() throws FileNotFoundException {
        /*
         * Replace token file path
         */
        File tokenFile = new File("C:\\Users\\Radu\\Desktop\\Java\\Discord Bot\\src\\main\\externalFiles\\token.txt");
        Scanner tokenScanner = new Scanner(tokenFile);
        return tokenScanner.nextLine();
    }
    private static void setRichPresence(@NotNull JDA jda) {
        Presence presence = jda.getPresence();
        presence.setActivity(Activity.playing("~help"));
        presence.setStatus(OnlineStatus.ONLINE);
    }
}
