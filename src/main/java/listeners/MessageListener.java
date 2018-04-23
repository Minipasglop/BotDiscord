package listeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.ChatCommandParser;
import utils.CommandHandler;

public class MessageListener extends ListenerAdapter {

    private ChatCommandParser parser;

    public MessageListener(){
        this.parser = new ChatCommandParser();
        CommandHandler.getInstance();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        CommandHandler.handleCommand(this.parser.parse(event.getMessage().getContentRaw(),event));

    }
}
