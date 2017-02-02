package net.minipasglop.bot;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.guild.member.GuildMemberBanEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberLeaveEvent;

import java.io.*;
import java.text.SimpleDateFormat;



public class UserUpdateStatusEventListener {


    public UserUpdateStatusEventListener(){}

    public void useJoinEvent(GuildMemberJoinEvent e) {
        messageBienvenueJoinServeur(e.getUser(),e.getGuild().getPublicChannel());
    }

    public void useLeaveEvent(Event e){
        if(e instanceof GuildMemberBanEvent){
            GuildMemberBanEvent ev = (GuildMemberBanEvent) e;
            messageDepartServeur(ev.getUser(),ev.getGuild().getPublicChannel(),"Ban");
        }
        else if(e instanceof GuildMemberLeaveEvent){
            GuildMemberLeaveEvent ev = (GuildMemberLeaveEvent) e;
            messageDepartServeur(ev.getUser(),ev.getGuild().getPublicChannel(),"Leave");
        }
    }

    public void messageBienvenueJoinServeur(User u,TextChannel c){
        c.sendMessage("Bienvenue à : " + u.getAsMention() + " [Join}");
    }

    public void messageDepartServeur(User u, TextChannel c,String type){
        c.sendMessage("Bienvenue à : " + u.getAsMention() + " ["+type+"}");
    }

}

