package net.minipasglop.bot;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;

import java.util.List;
import java.util.Vector;

/*
Classe que je qualifierai "d'intermédiaire"... Elle permets de récupérer des infos depuis la classe Main, sans que les autres classes y aient accés directement. Encapsulation sisi
 */


public class Tools {

    public String getMentionFromMess(Message message) {
        List<User> userMention = message.getMentionedUsers();
        StringBuilder builder = new StringBuilder();
        builder.append(userMention.get(0).getId());
        String mentionsMessage = builder.toString();
        return "<@" + mentionsMessage + ">";
    }//Renvoie l'utilisateur mentionné afin que le bot puisse également utiliser la mention a partir d'un message

    public static String getMentionFromUser(User user) {
        String idUser = user.getId();
        return "<@" + idUser + ">";
    }//getMenntionFromUser()
}
