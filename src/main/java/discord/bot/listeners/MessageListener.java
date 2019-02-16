package discord.bot.listeners;

import discord.bot.utils.commands.ChatCommandParser;
import discord.bot.utils.commands.CommandHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class MessageListener extends ListenerAdapter {

    private ChatCommandParser parser;
    private Logger logger = Logger.getLogger(MessageListener.class);

    public MessageListener(){
        this.parser = new ChatCommandParser();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        try {
            if (event.getMessage().getContentRaw() != null && !event.getMessage().getContentRaw().isEmpty() && !event.getGuild().getId().isEmpty()) {
                CommandHandler.getInstance().handleCommand(event.getGuild().getId(), this.parser.parse(event.getMessage().getContentRaw(), event));
            }
        }catch (Exception e){
            logger.log(Level.ERROR, event.getMessage().getContentRaw(), e);
        }
    }
}
