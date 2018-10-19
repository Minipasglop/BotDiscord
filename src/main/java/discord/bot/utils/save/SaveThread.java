package discord.bot.utils.save;

import discord.bot.BotGlobalManager;
import net.dv8tion.jda.core.entities.Guild;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


//mechanics from https://github.com/thibautbessone

public class SaveThread extends Thread {

    private static Logger logger = Logger.getLogger(SaveThread.class);

    public void run() {
        Timer timer = new Timer ();
        TimerTask autoSaveTask = new TimerTask () {
            @Override
            public void run () {
                List<Guild> guildList = BotGlobalManager.getServers();
                ServerPropertiesJSONUpdate saver;
                for(int i = 0; i < guildList.size(); i++){
                    saver = new ServerPropertiesJSONUpdate(guildList.get(i).getId());
                    System.runFinalization();
                    System.gc();
                }
                logger.log(Level.INFO,"Propriétés sauvegardées.");
            }
        };
        timer.scheduleAtFixedRate(autoSaveTask, 1000*60*3, 1000*60*60);
    }
}