package discord.bot.command.bot.managing;

import discord.bot.BotGlobalManager;
import discord.bot.command.ICommand;
import discord.bot.utils.ServerPropertiesJSONUpdate;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class ForcePropertiesSaveCommand implements ICommand {

    private String HELP = "This command allows the Owner to force the save of properties before a reboot. \nUsage: `!saveProperties`";
    private String COMMAND_SUCCESS = "Successfully saved properties files";
    private String NOT_ALLOWED = "You must be the bot Owner in order to do that!";

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
            event.getAuthor().openPrivateChannel().queue((privateChannel -> privateChannel.sendMessage(COMMAND_SUCCESS).queue()));
        }else {
            event.getMessage().delete().queue();
            event.getAuthor().openPrivateChannel().queue((privateChannel -> privateChannel.sendMessage(NOT_ALLOWED).queue()));
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        if (!success) {
            event.getTextChannel().sendMessage(help()).queue();
        }
    }
}
