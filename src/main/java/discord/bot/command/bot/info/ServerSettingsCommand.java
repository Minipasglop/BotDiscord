package discord.bot.command.bot.info;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.save.PropertyEnum;
import discord.bot.utils.save.ServerPropertiesManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.awt.*;

public class ServerSettingsCommand extends ICommand {

    private final String HELP = "The command `"+ this.commandName +"` displays the current server settings. \nUsage: `!" + this.commandName + "`";
    private final String BLANK_PROPERTY = "Not set yet";
    private final String PROPERTY_ON = "On";
    private final String PROPERTY_OFF = "Off";
    private static Logger logger = Logger.getLogger(ServerSettingsCommand.class);


    public ServerSettingsCommand(String commandName) {
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
        try {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor("Server settings");
            builder.setColor(Color.ORANGE);
            builder.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
            for (PropertyEnum property : PropertyEnum.values()) {
                builder.addField(property.getPropertyNameForUser(), formatPropertyValue(ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(event.getGuild().getId(),property.getPropertyName())), true);
            }
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(), builder.build());
        }catch (Exception e){
            logger.log(Level.ERROR, event.getMessage(), e);
        }
    }

    private String formatPropertyValue(String property) {
        if(property.isEmpty()) {
            return BLANK_PROPERTY;
        }
        else if(("true").equals(property)){
            return PROPERTY_ON;
        }else if(("false").equals(property)){
            return PROPERTY_OFF;
        }
        return property;
    }

    @Override
    public String help() {
        return HELP;
    }

}
