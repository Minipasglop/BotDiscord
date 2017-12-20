package net.minipasglop.bot.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.minipasglop.bot.IBotCommand;
import net.minipasglop.bot.MessageReceivedEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CatCommand implements IBotCommand {


    @Override
    public void call(String args, MessageReceivedEvent event) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL("http://random.cat/meow").openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            byte[] bytes = new byte[1024];
            int tmp;
            String chaine, chaine2;
            String mess = null;
            while ((tmp = bis.read(bytes)) != -1) {
                chaine = new String(bytes, 0, tmp);
                chaine2 = chaine;
                mess = chaine.substring(chaine.indexOf("http"),14) + "//random.cat/i" + chaine2.substring(32,chaine2.indexOf("\"}"));
            }
            conn.disconnect();
            if(mess.isEmpty()) return;
            event.getTextChannel().sendMessage(mess).queue();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
