package discord.bot.command.misc;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.DogApiCall;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DogCommand extends ICommand {

    private final String HELP = "Get a nice :dog: pic. \nUsage : `!" + this.commandName + "`";
    private final String FAILED = "Failed to load a cute :dog: pic... :sweat: ";

    public DogCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (args.length != 0) {
            return false;
        } else return true;    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String imageUri = DogApiCall.getInstance().getRandomDogImage();
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
