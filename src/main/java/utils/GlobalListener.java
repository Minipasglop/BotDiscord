package utils;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.user.UserAvatarUpdateEvent;
import net.dv8tion.jda.core.events.user.UserNameUpdateEvent;
import net.dv8tion.jda.core.hooks.EventListener;


public class GlobalListener implements EventListener {

    private static GlobalListener instance;
    private ChatCommandParser parser;


    public static GlobalListener getInstance(){
        if(instance == null){
            instance = new GlobalListener();
        }
        return instance;
    }

    private GlobalListener(){
        this.parser = new ChatCommandParser();
        CommandHandler.getInstance();
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof MessageReceivedEvent) {
            MessageReceivedEvent e = (MessageReceivedEvent) event;
            CommandHandler.handleCommand(this.parser.parse(e.getMessage().getContentRaw(),e));
        }//Partie relative au listener gérant les commandes

        else if (event instanceof GuildMemberJoinEvent) {
        }// Partie relative au listener gerant le bonjour personnalise

        else if(event instanceof GuildBanEvent){

        }

        else if(event instanceof GuildMemberLeaveEvent){

        }

        else if (event instanceof UserAvatarUpdateEvent) {

        }//Partie relative au listener gérant les modifications d'avatar

        else if (event instanceof UserNameUpdateEvent){

        }//Partie relative au listener gérant les modifications de nom d'utilisateur

    }//
}
