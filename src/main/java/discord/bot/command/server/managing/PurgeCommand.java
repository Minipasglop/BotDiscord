package discord.bot.command.server.managing;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.misc.SharedStringEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.log4j.Logger;

import java.time.OffsetDateTime;
import java.util.List;

public class PurgeCommand extends ICommand {

    private final String HELP = "Deletes 2 weeks old messages from the current text channel. \nUsage :`!"+ this.commandName +"`";
    private final String ACTION_PERFORMED = "2 weeks old messages have been deleted :see_no_evil:";
    private final String ACTION_FAILED = "Failed deleting the chat room.";
    private static Logger logger = Logger.getLogger(PurgeCommand.class);


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
                    if(history.get(i).getTimeCreated().isBefore(OffsetDateTime.now().minusDays(15))){
                        history = history.subList(0, i);
                        break;
                    }
                }
                currChannel.deleteMessages(history).queue(null, throwable -> handleFailure(event));
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(), ACTION_PERFORMED);
            }else {
                event.getMessage().delete().queue(null, throwable -> handleFailure(event));
                MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(), SharedStringEnum.NOT_ALLOWED.getSharedString(), event.getTextChannel(), SharedStringEnum.NOT_ALLOWED.getSharedString());
            }
        }catch (IllegalArgumentException e){
            MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(), ACTION_FAILED, event.getTextChannel(), ACTION_FAILED);
        }
    }

    private void handleFailure(MessageReceivedEvent event){
        MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(), ACTION_FAILED);
        logger.error("Failed to delete some messages on server : " + event.getGuild().getId() + " / " + event.getGuild().getName());
    }

    @Override
    public String help() {
        return HELP;
    }

}