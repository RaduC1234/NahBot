package me.RaduCapatina.Bot;

import Commands.Spam;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.RaduCapatina.Commands.AmongUs;
import me.RaduCapatina.Commands.AutoVoiceChannel;
import me.RaduCapatina.Commands.Owner;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class Bot {

    public static final String prefix = "~";    // this String stores the command prefix
    public static final String channelPrefix = "~~";
    public static boolean Setup = false;    //this var stores if the guild owner has initiated the welcome message commnand
    public static Vector<Member> voiceAdminList = new Vector<Member>(1);
    /*      This bot has a system that auto-generates voice channels.
     *
     *      It works by checking if someone has joined the special channel, them it creates another voice channel and
     * then it moves that user there. It also makes that User an "voiceAdmin" defined in voiceAdminList Vector.
     *
     *      If an admin Left the channel, the system picks an random user.
     *      If the channel is empty, it will be deleted.
     *
     *      Commands: ~~kick (kicks the User without been able to rejoin)
     *                ~~lock (locks the channel)
     *                ~~unlock (unlocks the channel)
     *
     *      Special prefix: "~~"
     *
     *      If you want to use it for your own server be sure to replace the "Create Voice channel" channel
     *  and the category that contains that channel.
     */

    public static void main(String args[]) throws Exception {
        JDA jda = JDABuilder.createDefault(readToken()).build();

        //using JDA-Util
        CommandClientBuilder builder = new CommandClientBuilder();
        EventWaiter eventWaiter = new EventWaiter();

        builder.setPrefix(prefix);
        //builder.setHelpWord("help");
        builder.setOwnerId("318102952168390656");
        builder.setActivity(Activity.playing("~~help"));
        builder.addCommand(new AmongUs(eventWaiter));

        CommandClient client = builder.build();
        jda.addEventListener(client);

        //using normal jda
        jda.addEventListener(new Owner());
        jda.addEventListener(new AutoVoiceChannel());
        jda.addEventListener(new Spam());
//        jda.addEventListener(new Initializer());

    }

    private static String readToken() throws FileNotFoundException {
        //replace token file path
        File tokenFile = new File("C:\\Users\\Radu\\Desktop\\Java\\Discord Bot\\src\\main\\externalFiles\\token.txt");
        Scanner tokenScanner = new Scanner(tokenFile);
        return tokenScanner.nextLine();
    }
}
