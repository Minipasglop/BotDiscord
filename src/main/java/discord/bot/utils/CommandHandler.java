package discord.bot.utils;

import discord.bot.command.*;
import discord.bot.command.misc.PingCommand;
import discord.bot.command.misc.SoundPlayerCommand;
import discord.bot.command.misc.YoutubeCommand;
import discord.bot.command.server.managing.*;

import java.util.HashMap;
import java.util.Map;

//mechanics from https://github.com/thibautbessone

public class CommandHandler {

    private static Map<String, ICommand> commands = new HashMap<>();

    private static CommandHandler instance;

    private final static String PREFIX = "!";

    public static CommandHandler getInstance(){
        if(instance == null){
            instance = new CommandHandler();
        }
        return instance;
    }

    public static void handleCommand(ChatCommandParser.CommandAttributes cmdAttributes) {
        if(cmdAttributes.raw.startsWith(PREFIX)) {
            if(commands.containsKey(cmdAttributes.invoke)) {
                boolean safe = commands.get(cmdAttributes.invoke).called(cmdAttributes.args, cmdAttributes.event);
                if (safe) {
                    commands.get(cmdAttributes.invoke).action(cmdAttributes.args, cmdAttributes.event);
                    commands.get(cmdAttributes.invoke).executed(safe, cmdAttributes.event);
                } else {
                    commands.get(cmdAttributes.invoke).executed(safe, cmdAttributes.event);
                }
            }
        }
    }

    private CommandHandler(){
        commands.put("ping", new PingCommand());
        commands.put("purge", new PurgeCommand());
        commands.put("move",new MoveCommand());
        commands.put("kick",new KickCommand());
        commands.put("ban",new BanCommand());
        commands.put("mute",new MuteCommand());
        commands.put("addRole",new RoleAddingCommand());
        commands.put("sound",new SoundPlayerCommand());
        commands.put("yt",new YoutubeCommand());
    }
}
