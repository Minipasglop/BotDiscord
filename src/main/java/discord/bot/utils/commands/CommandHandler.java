package discord.bot.utils.commands;

import discord.bot.command.CommandEnum;
import discord.bot.command.ICommand;
import discord.bot.command.aliases.*;
import discord.bot.command.bot.info.InfoCommand;
import discord.bot.command.bot.info.InviteCommand;
import discord.bot.command.bot.info.ServerSettingsCommand;
import discord.bot.command.bot.info.WebsiteCommand;
import discord.bot.command.bot.managing.ForcePropertiesSaveCommand;
import discord.bot.command.bot.managing.SetGameCommand;
import discord.bot.command.bot.managing.SetPrefixCommand;
import discord.bot.command.misc.*;
import discord.bot.command.server.managing.*;
import discord.bot.utils.save.PropertyEnum;
import discord.bot.utils.save.ServerPropertiesManager;

import java.util.HashMap;
import java.util.Map;

//mechanics from https://github.com/thibautbessone

public class CommandHandler {

    private static Map<String,ICommand> soundCommands;
    private static Map<String, ICommand> serverCommands;
    private static Map<String, ICommand> miscCommands;
    private static Map<String, ICommand> ownerCommands;
    private String DEFAULT_PREFIX = "!";

    private static CommandHandler instance;

    public static CommandHandler getInstance(){
        if(instance == null){
            instance = new CommandHandler();
        }
        return instance;
    }

    public void handleCommand(String guildID,ChatCommandParser.CommandAttributes cmdAttributes) {
        String customPrefix = ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(guildID, PropertyEnum.PREFIX.getPropertyName());
        if((!customPrefix.isEmpty() && cmdAttributes.raw.startsWith(customPrefix)) || cmdAttributes.raw.startsWith(DEFAULT_PREFIX)) {
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

        soundCommands = new HashMap<>();
        soundCommands.put(CommandEnum.SOUND.getCommandName(), new SoundPlayerCommand(CommandEnum.SOUND.getCommandName()));
        soundCommands.put(CommandEnum.VOLUME.getCommandName(), new SoundVolumeCommand(CommandEnum.VOLUME.getCommandName()));
        soundCommands.put(CommandEnum.SKIP.getCommandName(), new SoundSkipCommand(CommandEnum.SKIP.getCommandName()));
        soundCommands.put(CommandEnum.STOP.getCommandName(), new SoundStopCommand(CommandEnum.STOP.getCommandName()));
        soundCommands.put(CommandEnum.LOOP.getCommandName(), new SoundLoopCommand(CommandEnum.LOOP.getCommandName()));
        soundCommands.put(CommandEnum.QUEUE.getCommandName(), new SoundQueueCommand(CommandEnum.QUEUE.getCommandName()));
        soundCommands.put(CommandEnum.PLAY_PAUSE.getCommandName(), new SoundPauseCommand(CommandEnum.PLAY_PAUSE.getCommandName()));
        soundCommands.put(CommandEnum.SHUFFLE.getCommandName(), new SoundShuffleCommand(CommandEnum.SHUFFLE.getCommandName()));

        serverCommands = new HashMap<>();
        serverCommands.put(CommandEnum.ADD_ROLE.getCommandName() ,new RoleAddingCommand(CommandEnum.ADD_ROLE.getCommandName()));
        serverCommands.put(CommandEnum.SET_AUTO_ROLE_ON_JOIN.getCommandName(), new SetAutoRoleOnJoinCommand(CommandEnum.SET_AUTO_ROLE_ON_JOIN.getCommandName()));
        serverCommands.put(CommandEnum.USER_EVENT_CHANNEL.getCommandName(), new SetUserEventChannelCommand(CommandEnum.USER_EVENT_CHANNEL.getCommandName()));
        serverCommands.put(CommandEnum.USER_EVENT_TOGGLING.getCommandName(), new SetUserEventStatusCommand(CommandEnum.USER_EVENT_TOGGLING.getCommandName()));
        serverCommands.put(CommandEnum.BAN.getCommandName(), new BanCommand(CommandEnum.BAN.getCommandName()));
        serverCommands.put(CommandEnum.HELP.getCommandName(), new HelpCommand(CommandEnum.HELP.getCommandName()));
        serverCommands.put(CommandEnum.KICK.getCommandName(), new KickCommand(CommandEnum.KICK.getCommandName()));
        serverCommands.put(CommandEnum.MOVE.getCommandName(), new MoveCommand(CommandEnum.MOVE.getCommandName()));
        serverCommands.put(CommandEnum.MUTE.getCommandName(), new MuteCommand(CommandEnum.MUTE.getCommandName()));
        serverCommands.put(CommandEnum.PURGE.getCommandName(), new PurgeCommand(CommandEnum.PURGE.getCommandName()));
        serverCommands.put(CommandEnum.SET_PREFIX.getCommandName(), new SetPrefixCommand(CommandEnum.SET_PREFIX.getCommandName()));

        miscCommands = new HashMap<>();
        miscCommands.put(CommandEnum.INFO.getCommandName(), new InfoCommand(CommandEnum.INFO.getCommandName()));
        miscCommands.put(CommandEnum.INVITE.getCommandName(),new InviteCommand(CommandEnum.INVITE.getCommandName()));
        miscCommands.put(CommandEnum.WEBSITE.getCommandName(), new WebsiteCommand(CommandEnum.WEBSITE.getCommandName()));
        miscCommands.put(CommandEnum.SERVER_SETTINGS.getCommandName(), new ServerSettingsCommand(CommandEnum.SERVER_SETTINGS.getCommandName()));
        miscCommands.put(CommandEnum.PING.getCommandName(), new PingCommand(CommandEnum.PING.getCommandName()));
        miscCommands.put(CommandEnum.YOUTUBE_VIDEO_LINK.getCommandName(), new YoutubeCommand(CommandEnum.YOUTUBE_VIDEO_LINK.getCommandName()));
        miscCommands.put(CommandEnum.CAT_PICTURE.getCommandName(), new CatCommand(CommandEnum.CAT_PICTURE.getCommandName()));
        miscCommands.put(CommandEnum.DOG_PICTURE.getCommandName(), new DogCommand(CommandEnum.DOG_PICTURE.getCommandName()));
        miscCommands.put(CommandEnum.CHUCK_NORRIS_FACT.getCommandName(), new ChuckNorrisCommand(CommandEnum.CHUCK_NORRIS_FACT.getCommandName()));

        ownerCommands = new HashMap<>();
        ownerCommands.put(CommandEnum.SET_BOT_GAME.getCommandName(), new SetGameCommand(CommandEnum.SET_BOT_GAME.getCommandName()));
        ownerCommands.put(CommandEnum.FORCE_PROPERTIES_SAVING.getCommandName(), new ForcePropertiesSaveCommand(CommandEnum.FORCE_PROPERTIES_SAVING.getCommandName()));
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
