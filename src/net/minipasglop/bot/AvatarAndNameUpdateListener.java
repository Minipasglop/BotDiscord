package net.minipasglop.bot;



import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.user.UserAvatarUpdateEvent;
import net.dv8tion.jda.core.events.user.UserNameUpdateEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Classe gérant les events relatifs a la modification de nom d'utilisateur / avatar
public class AvatarAndNameUpdateListener {

    private BufferedWriter bw;

    public void useAva(UserAvatarUpdateEvent e){
        List<Guild> list = Main.getListeSalonBot();
        for(Guild t : list){
            try{
                Member userConcerne = t.getMember(e.getUser());
                t.getDefaultChannel().sendMessage(userConcerne.getUser().getAsMention() + " j'adore ton nouvel avatar :heart: :fire:").complete();
                return;
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }
    }// Partie relative au listener gérant le changement d'avatar

    public void useName(Event e) {
        User user = ((UserNameUpdateEvent) e).getUser();
        user.openPrivateChannel().complete().sendMessage("Jackson n'oublie pas... :wink:").complete();
        String txtDate = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(new Date());
        try {
            bw = new BufferedWriter(new FileWriter("Log.txt", true));
            bw.write("\nL'utilisateur : " + user.getName() + " d'id : " + user.getId() + " a change de nom le : " + txtDate + "\n");
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }//envoie un message a l'utilisateur qui viens de changer d'user name et stocke ce changement dans un fichier.
    }
}
