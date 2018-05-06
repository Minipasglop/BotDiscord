package command.server.managing;

import command.ICommand;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.PropertiesLoader;
import utils.RandomColorGenerator;

import java.util.Arrays;
import java.util.List;

public class RoleAddingCommand implements ICommand {

    private static PropertiesLoader config = new PropertiesLoader();
    private final String HELP = "Commande d'ajout d'un role. \nUsage : `!addRole @user nomDuRole`";
    private final String ACTION_PERFORMED_CREATE = "Créer + Ajouter role : ";
    private final String ACTION_PERFORMED_ADD = "Ajouter role : ";
    private final String NOT_ALLOWED = "Cette commande est désactivée pour le moment. Tu peux contacter Minipasglop#3347 pour plus d'informations.";
    private final String NOT_REQUIRED = "Le membre identifié possède déjà le rôle assigné.";
    private final String COMMAND_FAILED = "Il y a eu un problème lors de la création du rôle. Veuillez vous assurer que je suis Administrateur du serveur.";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length != 0 && args[0].equals("help") || args.length < 2 ) return false;
        else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(config.isRoleAddingCommandEnabled()){
            List<Member> targetedUsers = event.getMessage().getMentionedMembers();
               for (Member curr : targetedUsers) {
                try {
                    if(event.getGuild().getRolesByName(args[args.length - 1],true).isEmpty()) {
                        event.getGuild().getController().createRole().setName(args[args.length - 1]).setColor(RandomColorGenerator.generateRandomColor()).complete();
                        event.getGuild().getController().addRolesToMember(curr, event.getGuild().getRolesByName(args[args.length - 1], true)).queue();
                        System.out.println(ACTION_PERFORMED_CREATE + args[args.length - 1] + " a l'utilisateur " + curr.getUser().getName() + " sur le serveur : " + event.getGuild().getName());
                    }else if(!curr.getRoles().contains(event.getGuild().getRolesByName(args[args.length - 1],true).get(0))) {
                        event.getGuild().getController().addRolesToMember(curr, event.getGuild().getRolesByName(args[args.length - 1], true)).queue();
                        System.out.println(ACTION_PERFORMED_ADD + args[args.length - 1] + " a l'utilisateur " + curr.getUser().getName() + " sur le serveur : " + event.getGuild().getName());
                    }else {
                        event.getMessage().delete().queue();
                        PrivateChannel chanToTalk = event.getAuthor().openPrivateChannel().complete();
                        chanToTalk.sendMessage(NOT_REQUIRED).queue();
                    }
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