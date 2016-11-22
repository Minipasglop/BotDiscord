package net.minipasglop.bot;

import net.dv8tion.jda.OnlineStatus;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.user.UserOnlineStatusUpdateEvent;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by Junior on 21/10/2016.
 */
public class UserUpdateStatusEventListener {

    private static SimpleDateFormat h;

    private static BufferedWriter bw;

    public UserUpdateStatusEventListener() {
        h = new SimpleDateFormat ("HH");
    }

    private static boolean peutDireBonjour(User user) {
        try {
            Scanner scanner = new Scanner(new File("LogCo.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.equals(user.getId()))
                    return false;
            }
            scanner.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    private static void ecrireDansLog(User user) {
        try {
            bw = new BufferedWriter(new FileWriter("LogCo.txt",true));
            bw.write(Tools.newline + user.getId());
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void bonjourPersonnalise(TextChannel Salon, User user) {
        Date currentTime_1 = new Date();
        String heureString = h.format(currentTime_1);
        int heure = Integer.parseInt(heureString);
        if(heure > 20 || heure < 6 ) {
            if (user.getId().equals("160460873838755840") && peutDireBonjour(user)) {  //B4
                Salon.sendMessage("Bonchouèr " + Tools.getMentionFromUser(user) + " l'patron.");
                ecrireDansLog(user);
            }
            else if (user.getId().equals("213664666952400907") && peutDireBonjour(user)) {  //Cielou
                Salon.sendMessage("Bonchouèr " + Tools.getMentionFromUser(user) + " la :girl: du chan.");
                ecrireDansLog(user);
            }
            else if (user.getId().equals("218461869617184768") && peutDireBonjour(user)) { //PasGlop
                Salon.sendMessage("Bonchouèr à toi " + Tools.getMentionFromUser(user) + ", Ô créateur.");
                ecrireDansLog(user);
            }
            else if (user.getId().equals("213670267224850433") && peutDireBonjour(user)) {  //Filou
                Salon.sendMessage("Bon :cookie: du soir à toi " + Tools.getMentionFromUser(user) + ".");
                ecrireDansLog(user);
            }
            else if (user.getId().equals("213655383397498881") && peutDireBonjour(user)) {//Skel
                Salon.sendMessage("Bonjour à toi " + Tools.getMentionFromUser(user) + " le petit :rabbit:");
                ecrireDansLog(user);
            }
            else if (user.getId().equals("215072885960736773") && peutDireBonjour(user)) {//Gateau
                Salon.sendMessage("Bonchouèr ! Gateau ! :)");
            ecrireDansLog(user);
            }
            else if (user.getId().equals("222256376385241088")&& peutDireBonjour(user)) {
            Salon.sendMessage("Bonchouèr à toi frère de prénom du créateur aka " + Tools.getMentionFromUser(user));
            ecrireDansLog(user);
            }
            else if (user.getId().equals("198776707967090689") && peutDireBonjour(user)) {
            Salon.sendMessage("Yo tiltMeister  de la nuit aka " + Tools.getMentionFromUser(user));
            ecrireDansLog(user);
            }
        }
        else {
            if (user.getId().equals("160460873838755840") && peutDireBonjour(user)) {  //B4
                Salon.sendMessage("Chalut " + Tools.getMentionFromUser(user) + " l'patron.");
                ecrireDansLog(user);
            }
            else if (user.getId().equals("213664666952400907") && peutDireBonjour(user)) { //Cielou
                Salon.sendMessage("Coucou " + Tools.getMentionFromUser(user) + " la :girl: du chan.");
                ecrireDansLog(user);
            }
            else if (user.getId().equals("218461869617184768") && peutDireBonjour(user)) { //PasGlop
                Salon.sendMessage("Bonjour à toi " + Tools.getMentionFromUser(user) + ", Ô créateur.");
                ecrireDansLog(user);
            }
            else if (user.getId().equals("213670267224850433") && peutDireBonjour(user)) { //Filou
                Salon.sendMessage("Bon :cookie: à toi " + Tools.getMentionFromUser(user) + ".");
                ecrireDansLog(user);
            }
            else if (user.getId().equals("213655383397498881") && peutDireBonjour(user)) { //Skel
                Salon.sendMessage("Bonjour à toi " + Tools.getMentionFromUser(user) + " le petit :rabbit:");
                ecrireDansLog(user);
            }
            else if (user.getId().equals("215072885960736773") && peutDireBonjour(user)) { //Gateau
                Salon.sendMessage("Hey ! Gateau ! :)");
                ecrireDansLog(user);
            }
            else if (user.getId().equals("222256376385241088") && peutDireBonjour(user)) {
                Salon.sendMessage("Bonjour à toi frère de prénom du créateur aka " + Tools.getMentionFromUser(user));
                ecrireDansLog(user);
            }
            else if (user.getId().equals("198776707967090689") && peutDireBonjour(user)) {
                Salon.sendMessage("Yo tiltMeister aka " + Tools.getMentionFromUser(user));
                ecrireDansLog(user);
            }
        }

    }//Fonction gerant les bonjours personnalisés

    public void use(UserOnlineStatusUpdateEvent e) {
        if (e.getPreviousOnlineStatus().equals(OnlineStatus.OFFLINE) && Tools.getListeUsers().elementAt(0).contains(e.getUser())) {
            bonjourPersonnalise(Tools.getJda().getTextChannelById(Tools.getIdMainSalonB4()), e.getUser());
        } else if (e.getPreviousOnlineStatus().equals(OnlineStatus.OFFLINE) && Tools.getListeUsers().elementAt(1).contains(e.getUser())) {
            bonjourPersonnalise(Tools.getJda().getTextChannelById(Tools.getIdMainSalonMini()), e.getUser());
        }
    }
}

