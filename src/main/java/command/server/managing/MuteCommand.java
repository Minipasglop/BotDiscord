package command.server.managing;

import command.ICommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class MuteCommand implements ICommand {

    private final String HELP = "Commande pour mute un / plusieurs kikous \nUsage : `!mute @user1 @userB raison durée(minutes)`";
    private final String MUTE_MESSAGE = "Tu as été rendu muet car : ";
    private final String UNMUTE_MESSAGE = "Tu as été unmute. Sois sage maintenant !";
    private final String NOT_ALLOWED = "Tu n'es pas habilité à mute d'autres utilisateurs... Dommage :)";
    private final String ACTION_PERFORMED = "Rendre muet : ";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length < 3 && args[0].equals("help") || args.length < 3 ) return false;
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(event.getMember().getPermissions().contains(Permission.VOICE_MUTE_OTHERS)) {
            List<Member> targetedUsers = event.getMessage().getMentionedMembers();
            for (Member curr : targetedUsers) {
                event.getGuild().getController().setMute(curr, true).queue();
                System.out.println(ACTION_PERFORMED + curr.getEffectiveName() + " sur le serveur : " + event.getGuild().getName());
                PrivateChannel chanToTalk = curr.getUser().openPrivateChannel().complete();
                chanToTalk.sendMessage(MUTE_MESSAGE + args[args.length - 2] + " et ce pour " + args[args.length - 1] + " minute." + (Integer.parseInt(args[args.length - 1]) > 1 ? "s" : "")).queue();
                Runnable waitUntilDemute = () -> {
                    try {
                        Thread.sleep(Integer.parseInt(args[args.length - 1]) * 60000);
                        event.getGuild().getController().setMute(curr, false).queue();
                        chanToTalk.sendMessage(UNMUTE_MESSAGE).queue();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };
                waitUntilDemute.run();
            }
        } else {
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
