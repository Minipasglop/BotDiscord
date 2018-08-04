package discord.bot.command.server.managing;

import discord.bot.command.ICommand;
import discord.bot.utils.ServerPropertiesManager;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SetAutoRoleOnJoinCommand extends ICommand {

    private final String HELP = "Adds a role to an user. Creates the role if it doesn't exist, or use the one that exists. \nUsage : `!"+ this.commandName +" roleName`";
    private final String COMMAND_SUCCESS = "Successfully added the chosen role as default role on join.";
    private final String COMMAND_FAILED = "An unexpected error occured. Please make sure the role exist.";

    public SetAutoRoleOnJoinCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (args.length != 0 && args[0].equals("help") || args.length < 1) return false;
        else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String role = "";
        for(int i = 0; i  < args.length; ++i){
            role += args[i] + (i+1 < args.length ? " " : "");
        }
        if (!(event.getGuild().getRolesByName(role, true).get(0) == null)) {
            ServerPropertiesManager.getInstance().setPropertyForServer(event.getGuild().getId(),"autoRole",role);
            event.getTextChannel().sendMessage(COMMAND_SUCCESS).queue();
        } else {
            event.getMessage().delete().queue();
            PrivateChannel chanToTalk = event.getAuthor().openPrivateChannel().complete();
            chanToTalk.sendMessage(COMMAND_FAILED).queue();
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        if (!success) {
            event.getTextChannel().sendMessage(help()).queue();
        }
    }
}
