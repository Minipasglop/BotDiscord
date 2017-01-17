package net.minipasglop.bot;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.user.UserAvatarUpdateEvent;
import net.dv8tion.jda.events.user.UserNameUpdateEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//Classe gérant les events relatifs a la modification de nom d'utilisateur / avatar
public class AvatarAndNameUpdateListener {

    private BufferedWriter bw;

    public void useAva(UserAvatarUpdateEvent e){
        for(Guild chan : Main.getListeSalonBot()){
            if(chan.getUsers().contains(e.getUser())){
                Main.getJda().getTextChannels().get(0).sendMessage(e.getUser().getAsMention() + " j'adore ton nouvel avatar :heart: :fire:\" ");
            }
        }
    }// Partie relative au listener gérant le changement d'avatar

    public void useName(Event e) {
        User user = ((UserNameUpdateEvent) e).getUser();
        user.getPrivateChannel().sendMessage("Jackson n'oublie pas... :wink:");
        String txtDate = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(new Date());
        try {
            bw = new BufferedWriter(new FileWriter("Log.txt", true));
            bw.write("\nL'utilisateur : " + user.getUsername() + " d'id : " + user.getId() + " a change de nom le : " + txtDate + "\n");
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }//envoie un message a l'utilisateur qui viens de changer d'user name et stocke ce changement dans un fichier.
    }
}
