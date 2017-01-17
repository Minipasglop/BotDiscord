package net.minipasglop.bot;

import net.dv8tion.jda.OnlineStatus;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.user.UserOnlineStatusUpdateEvent;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


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
            bw.write("\n" + user.getId());
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
            if (peutDireBonjour(user)) {
                Salon.sendMessage("Bonchouèr à toi " + Tools.getMentionFromUser(user));
                ecrireDansLog(user);
            }
        }
        else {
            if (peutDireBonjour(user)) {
                Salon.sendMessage("Bonjour jeune " + Tools.getMentionFromUser(user) + " l'thug.");
                ecrireDansLog(user);
            }
        }

    }//Fonction gerant les bonjours personnalisés

    public void use(UserOnlineStatusUpdateEvent e) {
        if (e.getPreviousOnlineStatus().equals(OnlineStatus.OFFLINE)) {
            List<Guild> list = Main.getListeSalonBot();
            for(Guild t : list){
                if(t.getUsers().contains(e.getUser())){
                       bonjourPersonnalise(t.getPublicChannel(), e.getUser());
                    return;
                }
            }
        }
    }
}

