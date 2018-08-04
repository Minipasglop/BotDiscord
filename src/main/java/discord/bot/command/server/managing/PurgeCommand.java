package discord.bot.command.server.managing;

import discord.bot.command.ICommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class PurgeCommand extends ICommand {

    private final String HELP = "Deletes the most message of the text channel. \nUsage :`!"+ this.commandName +"`";
    private final String ACTION_PERFORMED = "ChatRoom has been cleaned :see_no_evil:";
    private final String NOT_ALLOWED = "You're not allowed to purge the chatroom... Sadly :)";
    private final String ACTION_FAILED = "Failed deleting the chat room.";

    public PurgeCommand(String commandName) {
        super(commandName);
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length != 0 && args[0].equals("help") || args.length !=0 ) return false;
        else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event)  {
        try {
        if(event.getMember().getPermissions().contains(Permission.MESSAGE_MANAGE)) {
            TextChannel currChannel = event.getTextChannel();
            List<Message> history = currChannel.getIterableHistory().complete();
            currChannel.deleteMessages(history).queue();
            event.getTextChannel().sendMessage(ACTION_PERFORMED).queue();
        }else {
            event.getMessage().delete().queue();
            PrivateChannel chanToTalk = event.getAuthor().openPrivateChannel().complete();
            chanToTalk.sendMessage(NOT_ALLOWED).queue();
            }
        }catch (IllegalArgumentException e){
            PrivateChannel chanToTalk = event.getAuthor().openPrivateChannel().complete();
            chanToTalk.sendMessage(ACTION_FAILED).queue();
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        if(!success) {
            event.getTextChannel().sendMessage(help()).queue();
        }
    }
}