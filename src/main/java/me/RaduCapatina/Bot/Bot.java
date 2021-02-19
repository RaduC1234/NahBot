package me.RaduCapatina.Bot;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.RaduCapatina.Commands.AutoVoiceChannel;
import me.RaduCapatina.Commands.Help;
import me.RaduCapatina.Commands.UserInfo;
import me.RaduCapatina.DeadCode.AmongUs;
import me.RaduCapatina.Haha;
import me.RaduCapatina.NahGuild.Owner;
import me.RaduCapatina.Setup.JSONManager;
import me.RaduCapatina.Setup.SetupOOBE1;
import me.RaduCapatina.Setup.SetupOOBE2;
import me.RaduCapatina.Voice.VoiceConnect;
import me.RaduCapatina.Voice.VoiceDisconnect;
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


    public static void main(String[] args) throws Exception {

        JDA jda = JDABuilder.createDefault(readToken())/*.enableIntents(GatewayIntent.GUILD_MEMBERS)*/.build();

        //using JDA-Util
        CommandClientBuilder builder = new CommandClientBuilder();
        EventWaiter eventWaiter = new EventWaiter();

        builder.setPrefix(prefix);
        builder.useHelpBuilder(false);
        builder.setOwnerId("318102952168390656");
        builder.setActivity(Activity.playing("~~help"));

        //JDA-Util Commands
        builder.addCommand(new SetupOOBE2(eventWaiter));
        builder.addCommand(new VoiceConnect());
        builder.addCommand(new VoiceDisconnect());
        jda.addEventListener(new AmongUs(eventWaiter));
       // builder.addCommand(new AmongUs(eventWaiter));


        CommandClient client = builder.build();
        jda.addEventListener(client);
        jda.addEventListener(eventWaiter);

        //using normal jda
        jda.addEventListener(new Owner());
        jda.addEventListener(new AutoVoiceChannel());
        jda.addEventListener(new Help());
        jda.addEventListener(new JSONManager());
        jda.addEventListener(new UserInfo());
        jda.addEventListener(new SetupOOBE1());
        jda.addEventListener(new Shutdown());
        jda.addEventListener(new Haha());
        //jda.addEventListener(new Moderation());



    }

    private static String readToken() throws FileNotFoundException {
        //replace token file path
        File tokenFile = new File(".\\src\\main\\externalFiles\\token.txt");
        Scanner tokenScanner = new Scanner(tokenFile);
        return tokenScanner.nextLine();
    }
}
