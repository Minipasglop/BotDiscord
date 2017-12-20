package net.minipasglop.bot;


import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;

public class UserUpdateStatusEventListener {


    public UserUpdateStatusEventListener(){}

    public void useJoinEvent(GuildMemberJoinEvent e) {
        messageBienvenueJoinServeur(e.getUser(),e.getGuild().getDefaultChannel());
    }

    public void useLeaveEvent(Event e){
        if(e instanceof GuildBanEvent){
            GuildBanEvent ev = (GuildBanEvent) e;
            messageDepartServeur(ev.getUser(),ev.getGuild().getDefaultChannel(),"Kick / Ban");
        }
        else if(e instanceof GuildMemberLeaveEvent){
            GuildMemberLeaveEvent ev = (GuildMemberLeaveEvent) e;
            messageDepartServeur(ev.getUser(),ev.getGuild().getDefaultChannel(),"Leave");
        }
    }

    public void messageBienvenueJoinServeur(User u, TextChannel c){
        c.sendMessage("Bienvenue à : " + u.getAsMention() + " [Join]").complete();
    }

    public void messageDepartServeur(User u, TextChannel c,String type){
        c.sendMessage("Adieu : " + u.getName() + " ["+type+"]").complete();
    }

}

