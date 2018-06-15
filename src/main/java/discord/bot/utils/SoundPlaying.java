package discord.bot.utils;

import javax.swing.*;

public class SoundPlaying  extends SwingWorker<Void, Object> {
    private AudioServerManager audioServerManager;

    @Override
    protected Void doInBackground() throws Exception {
        audioServerManager.handlePlaylist();
        return null;
    }

    SoundPlaying(AudioServerManager audioServerManager){
        this.audioServerManager = audioServerManager;
    }
}
