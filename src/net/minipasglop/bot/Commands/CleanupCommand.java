package net.minipasglop.bot.Commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.minipasglop.bot.IBotCommand;

import java.util.List;

public class CleanupCommand implements IBotCommand {

    @Override
    public void call(String args, MessageReceivedEvent event) {
        int countToDelete = Integer.parseInt(args) + 1;
        TextChannel channelToClean = event.getTextChannel();
        List<Message> history = channelToClean.getHistory().retrievePast(countToDelete).complete();
        channelToClean.deleteMessages(history).queue();
        channelToClean.sendTyping().queue();
        channelToClean.sendMessage((countToDelete - 1) + " messages ont été supprimés. ( +cleanup " + (countToDelete - 1) + " )").queue();
    }
}
