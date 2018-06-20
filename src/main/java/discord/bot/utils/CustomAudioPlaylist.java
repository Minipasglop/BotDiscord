package discord.bot.utils;

import java.util.ArrayList;
import java.util.List;

public class CustomAudioPlaylist {

    private List<String> playList;
    private boolean loop;
    private final String NO_MORE_SOUND_QUEUED = "No more sound queued.";


    public CustomAudioPlaylist(String track) {
        playList = new ArrayList<>();
        playList.add(track);
        loop = false;
    }

    public void loopOnTrack(boolean loop){
        this.loop = loop;
    }

    public boolean isLoopingOnTrack() { return this.loop; }

    public String getCurrentTrackURL(){
        return playList.get(0);
    }

    public boolean skipTrack(boolean forceSkip){
        if((playList.size() > 1 && !loop) || (playList.size() > 1 && forceSkip)){
            playList = playList.subList(1, playList.size());
            return true;
        }else if(playList.size() > 1){
            return true;
        }
        resetPlayList();
        return false;
    }

    public boolean hasMoreTrack(){
        return playList.size() > 1;
    }

    public void addTrack(String track){
        playList.add(track);
    }

    public void resetPlayList(){
        playList = new ArrayList<>();
    }

    public int getTrackAmount() { return playList.size(); }

    public String getNextTrackURL() {
        if(playList.size() > 1){
            return playList.get(1);
        }
        return NO_MORE_SOUND_QUEUED;
    }

}
