package discord.bot.utils.save;

import discord.bot.BotGlobalManager;
import net.dv8tion.jda.core.entities.Guild;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


//mechanics from https://github.com/thibautbessone

public class SaveThread extends Thread {

    public void run() {
        Timer timer = new Timer ();
        TimerTask autoSaveTask = new TimerTask () {
            @Override
            public void run () {
                List<Guild> guildList = BotGlobalManager.getServers();
                for(int i = 0; i < guildList.size(); i++){
                    new ServerPropertiesJSONUpdate().init(guildList.get(i).getId());
                    System.runFinalization();
                    System.gc();
                }
            }
        };
        timer.scheduleAtFixedRate(autoSaveTask, 1000*60*3, 1000*60*60);
    }
}