package discord.bot.command.misc;

import discord.bot.command.ICommand;
import discord.bot.utils.commands.CommandHandler;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.misc.SharedStringEnum;
import discord.bot.utils.save.PropertyEnum;
import discord.bot.utils.save.ServerPropertiesManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Map;

public class HelpCommand extends ICommand {

    private String HELP = "The command `help` displays the commands available at the moment. \nUsage: `!" + this.commandName + "`";
    private String MESSAGE_HEADER = "The commands available at the moment are listed below. \n";
    private String MESSAGE_FOOTER ="\nIf you need help, please mind joining the support server : https://discord.gg/MUaWKcu\nThanks for using *JacksonBot* :heart: :smirk:";

    public HelpCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (args.length != 0) {
            return false;
        } else return true;
    }

    private String getFormattedStringFromCommandMap(Map<String, ICommand> commandMap){
        String commandList = "";
        Object[] commandArray = commandMap.keySet().toArray();
        for(int i = 0; i < commandArray.length; i++) {
            String command = commandArray[i].toString();
            commandList +=  "`" + command + "`" + "\t";
        }
        return commandList;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String soundCommandsList = getFormattedStringFromCommandMap(CommandHandler.getSoundCommands());
        String serverCommandsList = getFormattedStringFromCommandMap(CommandHandler.getServerCommands());
        String miscCommandsList = getFormattedStringFromCommandMap(CommandHandler.getMiscCommands());
        String ownerCommandsList = getFormattedStringFromCommandMap(CommandHandler.getOwnerCommands());
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(event.getJDA().getSelfUser().getName());
        builder.setColor(Color.ORANGE);
        builder.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
        builder.addField("Help :bulb: ", MESSAGE_HEADER + "All commands must be prefixed with a `" +
                ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(event.getGuild().getId(), PropertyEnum.PREFIX.getPropertyName()) + "`.\n To obtain more " +
                "information on a command, please type `" + ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(event.getGuild().getId(), PropertyEnum.PREFIX.getPropertyName()) + "command help`\n\n", true);
        builder.addBlankField(true);
        builder.addField("Sound commands :loudspeaker:", soundCommandsList + "\n", true);
        builder.addField("Server commands :desktop:", serverCommandsList + "\n", true);
        builder.addField("Misc commands :keyboard:", miscCommandsList + "\n", false);
        builder.addField("Owner only commands :warning:", ownerCommandsList + "\n", true);
        builder.addField("More infos :file_folder:", MESSAGE_FOOTER,false);
        MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(), builder.build(), event.getTextChannel(), SharedStringEnum.NO_PRIVATE_CHANNEL_OPEN.getSharedString());
    }

    @Override
    public String help() {
        return HELP;
    }

}