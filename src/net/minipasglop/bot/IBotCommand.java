package net.minipasglop.bot;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public interface IBotCommand{

    void call(String args, MessageReceivedEvent event);
}
