package discord.bot.command;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class ICommand {
    protected String commandName;

    public ICommand(String commandName){
        this.commandName = commandName;
    }
    public abstract boolean called(String[] args, MessageReceivedEvent event);
    public abstract void action(String[] args, MessageReceivedEvent event);
    public abstract String help();
    public abstract void executed(boolean success, MessageReceivedEvent event);
}