package discord.bot.command.bot.info;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class InviteCommand extends ICommand {

    private String HELP = "The command `"+ this.commandName +"` sends an invitation link in order to add the bot to any server you're admin on. \nUsage: `!" + this.commandName + "`";
    private String INVITATION_LINK = "https://discordapp.com/oauth2/authorize?client_id=457256267069784102&scope=bot&permissions=-1";

    public InviteCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (args.length != 0 && args[0].equals("help") || args.length != 0) {
            return false;
        } else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Invitation Link");
        builder.setColor(Color.ORANGE);
        builder.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
        builder.addField("", INVITATION_LINK, true);
        MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(), builder.build());
    }


    @Override
    public String help() {
        return HELP;
    }

}
