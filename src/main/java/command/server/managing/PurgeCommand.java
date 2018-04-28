package command.server.managing;

import command.ICommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class PurgeCommand implements ICommand {

    private final String HELP = "Commande de clean d'un salon. . \nUsage : `!purge`";
    private final String ACTION_PERFORMED = "La salle de chat a été nettoyée :see_no_evil:";
    private final String NOT_ALLOWED = "Tu n'es pas habilité à clean le chat... Dommage :)";


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length != 0 && args[0].equals("help") || args.length !=0 ) return false;
        else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event)  {
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