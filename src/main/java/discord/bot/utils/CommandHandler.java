package discord.bot.utils;

import discord.bot.command.ICommand;
import discord.bot.command.aliases.*;
import discord.bot.command.bot.info.InfoCommand;
import discord.bot.command.bot.managing.ForcePropertiesSaveCommand;
import discord.bot.command.bot.managing.SetGameCommand;
import discord.bot.command.misc.HelpCommand;
import discord.bot.command.misc.PingCommand;
import discord.bot.command.misc.SoundPlayerCommand;
import discord.bot.command.misc.YoutubeCommand;
import discord.bot.command.server.managing.*;

import java.util.HashMap;
import java.util.Map;

//mechanics from https://github.com/thibautbessone

public class CommandHandler {

    private static Map<String,ICommand> soundCommands;
    private static Map<String, ICommand> serverCommands;
    private static Map<String, ICommand> miscCommands;
    private static Map<String, ICommand> ownerCommands;

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
            Map<String, ICommand> localMap = new HashMap<>();
            if(soundCommands.containsKey(cmdAttributes.invoke)) localMap = soundCommands;
            if(serverCommands.containsKey(cmdAttributes.invoke)) localMap = serverCommands;
            if(miscCommands.containsKey(cmdAttributes.invoke)) localMap = miscCommands;
            if(ownerCommands.containsKey(cmdAttributes.invoke)) localMap = ownerCommands;
            if(!localMap.isEmpty()) {
                boolean safe = localMap.get(cmdAttributes.invoke).called(cmdAttributes.args, cmdAttributes.event);
                if (safe) {
                    localMap.get(cmdAttributes.invoke).action(cmdAttributes.args, cmdAttributes.event);
                    localMap.get(cmdAttributes.invoke).executed(safe, cmdAttributes.event);
                } else {
                    localMap.get(cmdAttributes.invoke).executed(safe, cmdAttributes.event);
                }
            }
        }
    }

    private CommandHandler(){
        SoundPlayerCommand soundPlayerCommandReference = new SoundPlayerCommand();

        soundCommands = new HashMap<>();
        soundCommands.put("sound", soundPlayerCommandReference);
        soundCommands.put("vol", new SoundVolumeCommand(soundPlayerCommandReference.getAudioServerManagers()));
        soundCommands.put("skip", new SkipSoundCommand(soundPlayerCommandReference.getAudioServerManagers()));
        soundCommands.put("stop", new StopSoundCommand(soundPlayerCommandReference.getAudioServerManagers()));
        soundCommands.put("loop", new SoundLoopCommand(soundPlayerCommandReference.getAudioServerManagers()));
        soundCommands.put("queue", new QueueCommand(soundPlayerCommandReference.getAudioServerManagers()));

        serverCommands = new HashMap<>();
        serverCommands.put("addRole" ,new RoleAddingCommand());
        serverCommands.put("setAutoRole", new SetAutoRoleOnJoinCommand());
        serverCommands.put("greetingschannel", new SetUserEventChannelCommand());
        serverCommands.put("greetingsmessage", new SetUserEventStatusCommand());
        serverCommands.put("ban", new BanCommand());
        serverCommands.put("help", new HelpCommand());
        serverCommands.put("kick", new KickCommand());
        serverCommands.put("move", new MoveCommand());
        serverCommands.put("mute", new MuteCommand());
        serverCommands.put("purge", new PurgeCommand());

        miscCommands = new HashMap<>();
        miscCommands.put("info", new InfoCommand());
        miscCommands.put("ping", new PingCommand());
        miscCommands.put("yt", new YoutubeCommand());

        ownerCommands = new HashMap<>();
        ownerCommands.put("setGame", new SetGameCommand());
        ownerCommands.put("saveProperties", new ForcePropertiesSaveCommand());
    }


    public static Map<String, ICommand> getSoundCommands() {
        return soundCommands;
    }

    public static Map<String, ICommand> getServerCommands() {
        return serverCommands;
    }

    public static Map<String, ICommand> getMiscCommands() {
        return miscCommands;
    }

    public static Map<String, ICommand> getOwnerCommands() {
        return ownerCommands;
    }
}
