package discord.bot.listeners;

import discord.bot.utils.commands.ChatCommandParser;
import discord.bot.utils.commands.CommandHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    private ChatCommandParser parser;

    public MessageListener(){
        this.parser = new ChatCommandParser();
        CommandHandler.getInstance();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        CommandHandler.handleCommand(event.getGuild().getId(),this.parser.parse(event.getMessage().getContentRaw(),event));
    }
}
