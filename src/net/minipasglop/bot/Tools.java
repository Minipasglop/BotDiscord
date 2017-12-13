package net.minipasglop.bot;

import net.dv8tion.jda.core.entities.User;

public class Tools {


    public static String getMentionFromUser(User user) {
        String idUser = user.getId();
        return "<@" + idUser + ">";
    }//getMenntionFromUser()
}
