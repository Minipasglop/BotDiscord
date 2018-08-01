package discord.bot.command.misc;

import discord.bot.BotGlobalManager;
import discord.bot.command.ICommand;
import discord.bot.utils.CommandHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Map;

public class HelpCommand implements ICommand {

    private String HELP = "The command `help` displays the commands available at the moment. \nUsage: `!help`";
    private String MESSAGE_HEADER = "The commands available at the moment are listed below. All commands must be prefixed with a `!`. \nTo obtain more information on a command, just type `!command help`\n\n";
    private String MESSAGE_FOOTER ="\nIf you need help, please mind joining the support server : https://discord.gg/MUaWKcu\nThanks for using *Jackson* :heart: :smirk:";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (args.length != 0 && args[0].equals("help") || args.length != 0) {
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
        builder.addField("Help :bulb: ", MESSAGE_HEADER, true);
        builder.addField("Sound commands :loudspeaker:", soundCommandsList + "\n̔̏", true);
        builder.addField("Server commands :desktop:", serverCommandsList + "\n̔̏", true);
        builder.addField("Misc commands :keyboard:", miscCommandsList + "\n̔̏", true);
        builder.addField("Owner only commands :warning:", ownerCommandsList + "\n̔̏", true);
        builder.addField("More infos :file_folder:", MESSAGE_FOOTER,true);
        event.getAuthor().openPrivateChannel().queue((channel) -> channel.sendMessage(builder.build()).queue());

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