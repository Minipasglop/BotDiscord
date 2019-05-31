package discord.bot.command.bot.info;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class WebsiteCommand extends ICommand {

    private final String HELP = "JacksonBot's website \nUsage: `!" + this.commandName + "`";
    private final String WEBSITE_URI = "https://jacksonbot.com";

    public WebsiteCommand(String commandName) {
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
        builder.setAuthor("JacksonBot's Website");
        builder.setColor(Color.ORANGE);
        builder.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
        builder.addField("", WEBSITE_URI, true);
        MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(), builder.build());
    }


    @Override
    public String help() {
        return HELP;
    }

}
