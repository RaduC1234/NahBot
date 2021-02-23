package me.RaduCapatina.Setup;

import me.RaduCapatina.Bot.Bot;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.List;

public class JSONManager extends ListenerAdapter {
    public static JsonRoles returnRoles(String serverId) {
        boolean usePublicRole = false;
        String accessRole = null;
        //List<String> moderators = null;

        JSONParser jsonParser = new JSONParser();
        try {
            //Read JSON file
            Object obj = jsonParser.parse(new FileReader(Bot.externalFilesPath + serverId + ".json"));

            JSONObject jsonObject = (JSONObject) obj;

            JSONObject rolesObject = (JSONObject) jsonObject.get("roles");

            usePublicRole = (Boolean) rolesObject.get("use_public_role");
            accessRole = rolesObject.get("access_role").toString();

            //TODO: READ MODERATORS

        } catch (FileNotFoundException e) {
            System.out.println("[BOT Event-Manager] Error: Cannot open guild file in function returnRoles");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new JsonRoles(usePublicRole, accessRole, null);
    }

    public static JSONChannel returnVoiceChannel(String serverId) {
        String categoryId = null;
        String channelId = null;

        JSONParser jsonParser = new JSONParser();
        try {
            //Read JSON file
            Object obj = jsonParser.parse(new FileReader(Bot.externalFilesPath + serverId + ".json"));

            JSONObject jsonObject = (JSONObject) obj;

            JSONObject channelSubSection = (JSONObject) jsonObject.get("voiceChannel");

            categoryId = channelSubSection.get("category").toString();
            channelId = channelSubSection.get("channel").toString();

        } catch (FileNotFoundException e) {
            System.out.println("[BOT Event-Manager] Error: Cannot open guild file in function returnVoiceChannel");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new JSONChannel(categoryId, channelId);
    }

    public static void setupAutoVoiceChannel(String serverId, JSONChannel jsonChannel) {
        JSONParser jsonParser = new JSONParser();
        try {
            //Read JSON file
            Object obj = jsonParser.parse(new FileReader(Bot.externalFilesPath + serverId + ".json"));

            JSONObject jsonObject = (JSONObject) obj;

            JSONObject channelSubSection = (JSONObject) jsonObject.get("voiceChannel");

            channelSubSection.put("channel", jsonChannel.channelId);
            channelSubSection.put("category", jsonChannel.categoryId);

            try (Writer out = new FileWriter(Bot.externalFilesPath + serverId + ".json")) {
                out.write(jsonObject.toJSONString());
            }

        } catch (FileNotFoundException e) {
            System.out.println("[BOT Event-Manager] Error: Cannot open guild file in function setupAutoVoiceChannel");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {

        JSONObject voiceChannel = new JSONObject();
        voiceChannel.put("setup", false);
        voiceChannel.put("category", "000000000000000000");
        voiceChannel.put("channel", "000000000000000000");

        JSONObject roles = new JSONObject();
        roles.put("use_public_role", false);
        roles.put("access_role", "000000000000000000");
        roles.put("bot_role", "0000000000000000");
        JSONArray moderators = new JSONArray();
        roles.put("moderators", moderators);

        JSONArray games = new JSONArray();

        JSONObject JSONFile = new JSONObject();
        JSONFile.put("voiceChannel", voiceChannel);
        JSONFile.put("roles", roles);
        JSONFile.put("games", games);

        try {
            FileWriter file = new FileWriter(Bot.externalFilesPath + event.getGuild().getId() + ".json");
            file.write(JSONFile.toJSONString());
            file.flush();
            file.close();
            System.out.println("[JSON-Manager] INFO: Guild JSON file created successfully for " + event.getGuild().getId() + ".");
        } catch (IOException e) {
            System.out.println("[JSON-Manager] Error: " + e.getMessage());
        }
    }

    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        File f = new File(Bot.externalFilesPath + event.getGuild().getId() + ".json");

        if (f.exists() && !f.isDirectory()) {
            try {
                if (f.delete()) {
                    System.out.println("[JSON-Manager] INFO: Deleted guild file number " + event.getGuild().getId() + ". Reason: Bot left the server.");
                }
            } catch (Exception e) {
                System.out.println("[JSON-Manager] Error: " + e.getMessage());
            }
        }
    }

    public static final class JsonRoles {
        private final boolean usePublicRole;
        private final String accessRole;
        private final List<String> moderators;

        JsonRoles(boolean usePublicRole, String accessRole, List<String> moderators) {
            this.usePublicRole = usePublicRole;
            this.accessRole = accessRole;
            this.moderators = moderators;
        }

        public boolean isUsePublicRole() {
            return usePublicRole;
        }

        public String getAccessRole() {
            return accessRole;
        }

        public List<String> getModerators() {
            return moderators;
        }
    }

    public static final class JSONChannel {
        private final String channelId;
        private final String categoryId;

        public JSONChannel(String categoryId, String channelId) {
            this.categoryId = categoryId;
            this.channelId = channelId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public String getChannelId() {
            return channelId;
        }
    }
}