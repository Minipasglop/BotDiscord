package discord.bot.utils;

import javax.swing.*;

public class SoundPlaying  extends SwingWorker<Void, Object> {
    private AudioGlobalManager audioGlobalManager;

    @Override
    protected Void doInBackground() throws Exception {
        audioGlobalManager.handlePlaylist();
        return null;
    }

    SoundPlaying(AudioGlobalManager audioGlobalManager){
        this.audioGlobalManager = audioGlobalManager;
    }
}
