package discord.bot.command.bot.managing;

import discord.bot.BotGlobalManager;
import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.misc.SharedStringEnum;
import discord.bot.utils.save.ServerPropertiesJSONUpdate;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class ForcePropertiesSaveCommand extends ICommand {

    private final String HELP = "This command allows the Owner to force the save of properties before a reboot. \nUsage: `!" + this.commandName + "`";
    private final String COMMAND_SUCCESS = "Successfully saved properties files";

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
            for (int i = 0; i < guildList.size(); i++) {
                new ServerPropertiesJSONUpdate().init(event.getGuild().getId());
            }
            MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(), COMMAND_SUCCESS, event.getTextChannel(), COMMAND_SUCCESS);
        }else {
            event.getMessage().delete().queue();
            MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(), SharedStringEnum.BOT_OWNER_ONLY.getSharedString(), event.getTextChannel(), SharedStringEnum.BOT_OWNER_ONLY.getSharedString());
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
