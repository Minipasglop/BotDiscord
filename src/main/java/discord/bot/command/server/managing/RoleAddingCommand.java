package discord.bot.command.server.managing;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.misc.RandomColorGenerator;
import discord.bot.utils.save.PropertiesLoader;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class RoleAddingCommand extends ICommand {

    private static PropertiesLoader config = new PropertiesLoader();
    private final String HELP = "Adds a role to an user. Creates the role if it doesn't exist, or use the one that exists. \nUsage : `!"+ this.commandName +" @user roleName`";
    private final String ACTION_PERFORMED_CREATE = "Créer + Ajouter role : ";
    private final String ACTION_PERFORMED_ADD = "Ajouter role : ";
    private final String NOT_ALLOWED = "This command has been disabled at the moment. Please make sure to check the support server to get the latests news.";
    private final String NOT_REQUIRED = "The user tagged already owns the role.";
    private final String COMMAND_FAILED = "An unexpected error occured. Please make sure Jackson has administrations role on the server.";

    public RoleAddingCommand(String commandName) {
        super(commandName);
    }

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
                        MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(),NOT_REQUIRED);
                    }
                } catch (Exception e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                    MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),COMMAND_FAILED);
                }
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