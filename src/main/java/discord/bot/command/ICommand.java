package discord.bot.command;


import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class ICommand {
    protected String commandName;

    public ICommand(String commandName){
        this.commandName = commandName;
    }
    public abstract boolean called(String[] args, MessageReceivedEvent event);
    public abstract void action(String[] args, MessageReceivedEvent event);
    public abstract String help();
    public void executed(boolean success, MessageReceivedEvent event){
        if(!success) {
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),help());
        }
    }
}