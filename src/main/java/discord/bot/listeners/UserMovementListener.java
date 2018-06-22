package discord.bot.listeners;

import discord.bot.utils.ServerPropertiesManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class UserMovementListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String serverId = event.getGuild().getId();
        if("true".equals(ServerPropertiesManager.getInstance().getPropertyFromServer(serverId,"userEventEnabled"))){
            TextChannel customizedChannel = event.getGuild().getTextChannelsByName(ServerPropertiesManager.getInstance().getPropertyFromServer(serverId,"userEventChannel"),true).get(0);
            if(customizedChannel != null){
                messageBienvenueJoinServeur(event.getUser(),customizedChannel);
            }else {
                messageBienvenueJoinServeur(event.getUser(), event.getGuild().getDefaultChannel());
            }
        }
        try{
            if(ServerPropertiesManager.getInstance().getPropertyFromServer(event.getGuild().getId(),"autoRole") != null) {
                autoRole(event.getGuild(), event.getMember());
            }
        }catch(InsufficientPermissionException e){
            System.out.println("Le bot n'a pas les permissions requises pour l'autoRole sur le serveur " + event.getGuild().getName());
        }
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        String serverId = event.getGuild().getId();
        if("true".equals(ServerPropertiesManager.getInstance().getPropertyFromServer(serverId,"userEventEnabled"))){
            TextChannel customizedChannel = event.getGuild().getTextChannelsByName(ServerPropertiesManager.getInstance().getPropertyFromServer(serverId,"userEventChannel"),true).get(0);
            if(customizedChannel != null){
                messageDepartServeur(event.getUser(),customizedChannel);
            }else {
                messageDepartServeur(event.getUser(), event.getGuild().getDefaultChannel());
            }
        }
    }

    private void autoRole(Guild serveur, Member user) {
        serveur.getController().addRolesToMember(user, serveur.getRolesByName(ServerPropertiesManager.getInstance().getPropertyFromServer(serveur.getId(),"autoRole"),true)).complete();
    }

    private void messageBienvenueJoinServeur(User user,TextChannel channel){
        channel.sendMessage(":punch: Welcome : " + user.getAsMention() + " [Join]").complete();
    }
    private void messageDepartServeur(User u, TextChannel c){
        c.sendMessage("See you in a better world : " + u.getName() + " [Leave] :cry:").complete();
    }


}


