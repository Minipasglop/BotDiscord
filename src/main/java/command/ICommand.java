package command;

//Code from https://github.com/thibautbessone

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface ICommand {
    boolean called(String[] args, MessageReceivedEvent event);
    void action(String[] args, MessageReceivedEvent event);
    String help();
    void executed(boolean success, MessageReceivedEvent event);
}