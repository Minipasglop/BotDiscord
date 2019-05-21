package discord.bot.command.misc;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.CatApiCall;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CatCommand extends ICommand {

    private final String HELP = "Get a nice :cat: pic. \nUsage : `!" + this.commandName + "`";
    private final String FAILED = "Failed to load a cute :cat: pic... :sweat: ";

    public CatCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (args.length != 0) {
            return false;
        } else return true;    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String imageUri = CatApiCall.getInstance().getRandomCatImage();
        if(imageUri != null && !imageUri.isEmpty()){
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),imageUri);
        }else {
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),FAILED);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
