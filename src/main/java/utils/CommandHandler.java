package utils;

import command.*;
import command.misc.PingCommand;
import command.server.managing.*;

import java.util.HashMap;
import java.util.Map;

//Code from https://github.com/thibautbessone

public class CommandHandler {

    private static Map<String, ICommand> commands = new HashMap<>();

    private static CommandHandler instance;

    public static CommandHandler getInstance(){
        if(instance == null){
            instance = new CommandHandler();
        }
        return instance;
    }

    public static void handleCommand(ChatCommandParser.CommandAttributes cmdAttributes) {
        if(commands.containsKey(cmdAttributes.invoke)) {
            boolean safe = commands.get(cmdAttributes.invoke).called(cmdAttributes.args, cmdAttributes.event);
            if(safe) {
                commands.get(cmdAttributes.invoke).action(cmdAttributes.args, cmdAttributes.event);
                commands.get(cmdAttributes.invoke).executed(safe, cmdAttributes.event);
            }
            else {
                commands.get(cmdAttributes.invoke).executed(safe, cmdAttributes.event);
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
    }
}
