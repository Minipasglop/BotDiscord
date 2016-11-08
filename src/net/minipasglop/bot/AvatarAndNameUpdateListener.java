package net.minipasglop.bot;

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

/**
 * Created by Junior on 21/10/2016.
 */
public class AvatarAndNameUpdateListener {

    private BufferedWriter bw;

    public void useAva(UserAvatarUpdateEvent e){
        if(Tools.getListeSalonsBot().get(0).getUsers().contains(e.getUser()))
            Tools.getJda().getTextChannelById(Tools.getIdMainSalonB4()).sendMessage(e.getUser().getAsMention() + " j'adore ton nouvel avatar :heart: :fire:");
        else if(Tools.getListeSalonsBot().get(1).getUsers().contains(e.getUser()))
            Tools.getJda().getTextChannelById(Tools.getIdMainSalonMini()).sendMessage(e.getUser().getAsMention() + " j'adore ton nouvel avatar :heart: :fire:");
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
        }
    }
}
