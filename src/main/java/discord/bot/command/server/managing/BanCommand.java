package discord.bot.command.server.managing;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.List;

public class BanCommand extends ICommand {
    private final String HELP = "Ban one / many users from the server if you're allowed to. \nUsage : `!"+ this.commandName +" @UserA @UserB @UserC ... duration Reason `";
    private final String BAN_MESSAGE = "You've been banned because of : ";
    private final String NOT_ALLOWED = "You're not allowed to ban other users... Sadly :)";
    private final String ACTION_PERFORMED = "Bannir : ";
    private static Logger logger = Logger.getLogger(BanCommand.class);

    public BanCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length != 0 && args[0].equals("help") || args.length < 3) return false;
        else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(event.getMember().getPermissions().contains(Permission.BAN_MEMBERS)) {
            List<Member> targetedUsers = event.getMessage().getMentionedMembers();
            for (Member curr : targetedUsers) {
                event.getGuild().getController().ban(curr, Integer.parseInt(args[args.length - 2]), BAN_MESSAGE + args[args.length - 1]).queue();
                logger.log(Level.INFO, ACTION_PERFORMED + curr.getUser().getName() + " par " + event.getAuthor().getName() + " sur le serveur : " + event.getGuild().getName());
            }
        }else{
            event.getMessage().delete().queue();
            MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(),NOT_ALLOWED);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
