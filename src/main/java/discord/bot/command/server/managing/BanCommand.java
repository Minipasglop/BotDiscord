package discord.bot.command.server.managing;

import discord.bot.command.ICommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class BanCommand implements ICommand {
    private final String HELP = "Ban one / many users from the server if you're allowed to. \nUsage : `!ban @UserA @UserB @UserC ... duration Reason `";
    private final String BAN_MESSAGE = "You've been banned because of : ";
    private final String NOT_ALLOWED = "You're not allowed to ban other users... Sadly :)";
    private final String ACTION_PERFORMED = "Bannir : ";

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
                System.out.println(ACTION_PERFORMED + curr.getUser().getName() + " par " + event.getAuthor().getName() + " sur le serveur : " + event.getGuild().getName());
            }
        }else{
            event.getMessage().delete().queue();
            PrivateChannel chanToTalk = event.getAuthor().openPrivateChannel().complete();
            chanToTalk.sendMessage(NOT_ALLOWED).queue();
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
