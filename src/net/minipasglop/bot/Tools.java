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

    private static final String idMainSalonB4 = "216566802153472000";
    private static final String idMainSalonMini = "218753166815133696";
    private static List<Guild> listeSalonsBot;
    private static Vector<List<User>> listeUsers;
    public static String newline = System.getProperty("line.separator");
    private static JDA jda;
    public static Vector<List<User>> getListeUsers() {
        return listeUsers;
    }
    public static void setListeUsers(Vector<List<User>> listeUsers) {
        Tools.listeUsers = listeUsers;
    }
    public static String getIdMainSalonB4() {
        return idMainSalonB4;
    }
    public static String getIdMainSalonMini() {
        return idMainSalonMini;
    }
    public static List<Guild> getListeSalonsBot() {
        return listeSalonsBot;
    }
    public static void setListeSalonsBot(List<Guild> listeSalonsBot) {
        Tools.listeSalonsBot = listeSalonsBot;
    }
    public static JDA getJda() {
        return jda;
    }
    public static void setJda(JDA jda) {
        Tools.jda = jda;
    }

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
