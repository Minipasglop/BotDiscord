package command.server.managing;

import command.ICommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class MoveCommand implements ICommand {

    private final String HELP = "Déplace un utilisateur d'un salon 1 vers un salon 2 . \nUsage : `!move @UserA @UserB @UserC ... SalonCible `";
    private final String COMMAND_FAILED = "Il y a eu une erreur au moment de déplacer l'utilisateur. Veuillez vous assurer qu'il soit déjà connecté à un salon vocal.";
    private final String NOT_ALLOWED = "Tu n'es pas habilité à déplacer d'autres utilisateurs... Dommage :)";


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length != 0 && args[0].equals("help") || args.length < 2) return false;
        else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(event.getMember().getPermissions().contains(Permission.VOICE_MOVE_OTHERS)) {
            List<Member> targetedUsers = event.getMessage().getMentionedMembers();
            for (Member curr : targetedUsers) {
                List<VoiceChannel> targetChannel = event.getGuild().getVoiceChannelsByName(args[args.length - 1], true);
                try {
                    event.getGuild().getController().moveVoiceMember(curr, targetChannel.get(0)).queue();
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                    event.getTextChannel().sendMessage(COMMAND_FAILED).queue();
                }
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
