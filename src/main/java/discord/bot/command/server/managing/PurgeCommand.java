package discord.bot.command.server.managing;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.List;

public class PurgeCommand extends ICommand {

    private final String HELP = "Deletes the most message of the text channel. \nUsage :`!"+ this.commandName +"`";
    private final String ACTION_PERFORMED = "2 weeks old messages have been deleted :see_no_evil:";
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
                for(int i = 0; i < history.size(); i++){
                    if(history.get(i).getCreationTime().isBefore(OffsetDateTime.now().minusDays(15))){
                        history = history.subList(0, i);
                        break;
                    }
                }
                currChannel.deleteMessages(history).queue();
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),ACTION_PERFORMED);
            }else {
                event.getMessage().delete().queue();
                MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(),NOT_ALLOWED);
            }
        }catch (IllegalArgumentException e){
            MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(),ACTION_FAILED);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}