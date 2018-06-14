package discord.bot.utils;

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
                ServerPropertiesJSONUpdate saver;
                for(int i = 0; i < guildList.size(); i++){
                    saver = new ServerPropertiesJSONUpdate(guildList.get(i).getId());
                    System.runFinalization();
                    System.gc();
                }
                System.out.println("Propriétés sauvegardées.");
            }
        };
        timer.schedule(autoSaveTask, 100, 1000*20*20);
    }
}