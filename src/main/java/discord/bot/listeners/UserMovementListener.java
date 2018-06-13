package discord.bot.listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class UserMovementListener extends ListenerAdapter {

    private final String ID_SERVEUR_MINI = "456585836826198016";

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        messageBienvenueJoinServeur(event.getUser(),event.getGuild().getDefaultChannel());
        if(event.getGuild().getId().equals(ID_SERVEUR_MINI)) {
            autoRole(event.getGuild(), event.getMember());
        }
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        messageDepartServeur(event.getUser(), event.getGuild().getDefaultChannel());
    }

    private void autoRole(Guild serveur, Member user) {
        serveur.getController().addRolesToMember(user, serveur.getRolesByName("Community",true)).complete();
    }

    private void messageBienvenueJoinServeur(User u, TextChannel c){
        c.sendMessage(":punch: Bienvenue à : " + u.getAsMention() + " [Join]").complete();
    }
    private void messageDepartServeur(User u, TextChannel c){
        c.sendMessage("Adieu : " + u.getName() + " [Leave] :cry:").complete();
    }


}


