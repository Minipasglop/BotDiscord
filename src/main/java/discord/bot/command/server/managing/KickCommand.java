package discord.bot.command.server.managing;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class KickCommand extends ICommand {
    private final String HELP = "Kick one / many users from the server if you're allowed to. \nUsage : `!"+ this.commandName +"@UserA @UserB @UserC ... Reason `";
    private final String KICK_MESSAGE = "You've been kicked because of : ";
    private final String NOT_ALLOWED = "You're not allowed to kick other users... Sadly :)";
    private final String ACTION_PERFORMED = "Exclure : ";

    public KickCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length != 0 && args[0].equals("help") || args.length < 2) return false;
        else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(event.getMember().getPermissions().contains(Permission.KICK_MEMBERS)) {
            List<Member> targetedUsers = event.getMessage().getMentionedMembers();
            for (Member curr : targetedUsers) {
                event.getGuild().getController().kick(curr, KICK_MESSAGE + args[args.length - 1]).queue();
                System.out.println(ACTION_PERFORMED + curr.getUser().getName() + " par " + event.getAuthor().getName() + " sur le serveur : " + event.getGuild().getName());
            }
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
