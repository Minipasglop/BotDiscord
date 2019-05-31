package discord.bot.command.bot.managing;

import discord.bot.BotGlobalManager;
import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.save.ServerPropertiesJSONUpdate;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class ForcePropertiesSaveCommand extends ICommand {

    private final String HELP = "This command allows the Owner to force the save of properties before a reboot. \nUsage: `!" + this.commandName + "`";
    private final String COMMAND_SUCCESS = "Successfully saved properties files";
    private final String NOT_ALLOWED = "You must be the bot Owner in order to do that!";

    public ForcePropertiesSaveCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (args.length != 0 && args[0].equals("help") || args.length != 0 ) {
            return false;
        } else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(event.getAuthor().getId().equals(BotGlobalManager.getConfig().getBotOwnerUserId())) {
            List<Guild> guildList = BotGlobalManager.getServers();
            ServerPropertiesJSONUpdate saver;
            for (int i = 0; i < guildList.size(); i++) {
                saver = new ServerPropertiesJSONUpdate(guildList.get(i).getId());
            }
            MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(),COMMAND_SUCCESS);
        }else {
            event.getMessage().delete().queue();
            MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(),NOT_ALLOWED);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
