package net.minipasglop.bot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.minipasglop.bot.Commands.CatCommand;
import net.minipasglop.bot.Commands.CleanupCommand;

import java.util.HashMap;
import java.util.Map;

public class BotCommandMatcher {

    private Map<String,IBotCommand> myCommandMap;

    private String prefix;

    private int commandCalledCount;

    private void callCommand(String command, MessageReceivedEvent event){
        String args = "";
        try {
            if(command.contains(" ")) {
                args = command.substring(command.indexOf(" ") + 1);
                command = command.substring(0,command.indexOf(" "));
            }
            myCommandMap.get(command).call(args, event);
            commandCalledCount++;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void match(Message command, MessageReceivedEvent event){
        if(command.getContent().startsWith(prefix)){
            String commandToCall = command.getContent().substring(command.getContent().indexOf(prefix) +1);
            callCommand(commandToCall, event);
        }
    }

    public int getCommandCalledCount(){
        return this.commandCalledCount;
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    }

    BotCommandMatcher(String prefix){
        this.myCommandMap = new HashMap<>();
        this.prefix = prefix;
        this.myCommandMap.put("cat",new CatCommand());
        this.myCommandMap.put("cleanup",new CleanupCommand());
    }
}
