package discord.bot.command.misc;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.DogApi;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
        String catImageUri = DogApi.getInstance().getRandomDogImage();
        if(catImageUri != null && !catImageUri.isEmpty()){
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),catImageUri);
        }else {
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),FAILED);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
