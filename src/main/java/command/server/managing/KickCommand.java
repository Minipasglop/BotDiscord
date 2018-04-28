package command.server.managing;

import command.ICommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class KickCommand implements ICommand {
    private final String HELP = "Kick un / plusieurs utilisateur(s) du seveur. \nUsage : `!kick @UserA @UserB @UserC ... Raison `";
    private final String KICK_MESSAGE = "Tu as été exclu car : ";
    private final String NOT_ALLOWED = "Tu n'es pas habilité à kick... Dommage :)";
    private final String ACTION_PERFORMED = "Eclure : ";

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
                System.out.println(ACTION_PERFORMED + curr.getNickname() + " par " + event.getAuthor().getName() + " sur le serveur : " + event.getGuild().getName());
            }
        }else {
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
