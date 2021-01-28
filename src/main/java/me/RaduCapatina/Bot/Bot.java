package me.RaduCapatina.Bot;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.RaduCapatina.Commands.*;
import me.RaduCapatina.DeadCode.AmongUs;
import me.RaduCapatina.DeadCode.Initializer;
import me.RaduCapatina.NahGuild.Owner;
import me.RaduCapatina.Setup.JSONSetup;
import me.RaduCapatina.Setup.SetupOOBE1;
import me.RaduCapatina.Setup.SetupOOBE2;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class Bot {

               
    public static final String prefix = "~~"; // this String stores the command prefix
    public static String externalFilesPath = ".\\src\\main\\externalFiles\\guildData\\";
    public static Vector<Member> voiceAdminList = new Vector<Member>(1);


    public static void main(String args[]) throws Exception {
        JDA jda = JDABuilder.createDefault(readToken()).build();

        //using JDA-Util
        CommandClientBuilder builder = new CommandClientBuilder();
        EventWaiter eventWaiter = new EventWaiter();

        builder.setPrefix(prefix);
        builder.useHelpBuilder(false);
        builder.setOwnerId("318102952168390656");
        builder.setActivity(Activity.playing("~~help"));
        builder.addCommand(new SetupOOBE2(eventWaiter));
        //builder.addCommand(new AmongUs(eventWaiter));


        CommandClient client = builder.build();
        jda.addEventListener(client);

        //using normal jda
        jda.addEventListener(new Owner());
        jda.addEventListener(new AutoVoiceChannel());
        jda.addEventListener(new Help());
        jda.addEventListener(new JSONSetup());
        jda.addEventListener(new UserInfo());
        jda.addEventListener(new SetupOOBE1());
        jda.addEventListener(new Shutdown());
        //jda.addEventListener(new Initializer());

    }

    private static String readToken() throws FileNotFoundException {
        //replace token file path
        File tokenFile = new File(".\\src\\main\\externalFiles\\token.txt");
        Scanner tokenScanner = new Scanner(tokenFile);
        return tokenScanner.nextLine();
    }
}
