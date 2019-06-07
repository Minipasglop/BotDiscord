package discord.bot.listeners;

import discord.bot.BotGlobalManager;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.save.PropertyEnum;
import discord.bot.utils.save.ServerPropertiesJSONUpdate;
import discord.bot.utils.save.ServerPropertiesManager;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GuildMovementListener extends ListenerAdapter {

    private static Logger logger = Logger.getLogger(GuildMovementListener.class);

    @Override
    public void onGuildJoin(GuildJoinEvent event){
        String joinMessage = "Bot has been added to server : " + event.getGuild().getName()  + " - " + event.getGuild().getId();
        Map<String,String> propertiesForJoinedServer = new HashMap<>();
        for(PropertyEnum property : PropertyEnum.values()){
            propertiesForJoinedServer.put(property.getPropertyName(), property.getDefaultValue());
        }
        ServerPropertiesManager.getInstance().setPropertiesForServer(event.getGuild().getId(), propertiesForJoinedServer);
        new ServerPropertiesJSONUpdate().init(event.getGuild().getId()); //Saves the property file
        MessageSenderFactory.getInstance().sendSafeMessage(Objects.requireNonNull(BotGlobalManager.getBotLogsChannel()), joinMessage);
        logger.log(Level.INFO, joinMessage);
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event){
        String leaveMessage = "Bot has been removed from server : " + event.getGuild().getName() + " - " + event.getGuild().getId();
        MessageSenderFactory.getInstance().sendSafeMessage(Objects.requireNonNull(BotGlobalManager.getBotLogsChannel()), leaveMessage);
        logger.log(Level.INFO, leaveMessage);
    }
}
