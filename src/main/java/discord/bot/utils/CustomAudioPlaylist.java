package discord.bot.utils;

import java.util.ArrayList;
import java.util.List;

public class CustomAudioPlaylist {

    private List<Track> playList;
    private final String NO_MORE_SOUND_QUEUED = "No more sound queued.";


    public CustomAudioPlaylist(Track track) {
        playList = new ArrayList<>();
        playList.add(track);
    }

    public void loopOnTrack(){ this.playList.get(0).revertLooping(); }

    public boolean isLoopingOnTrack() { return this.playList.get(0).isLooping(); }

    public String getCurrentTrackURL(){
        return playList.get(0).getVideoUrl();
    }

    public boolean skipTrack(){
        if(isLoopingOnTrack()){
            return true;
        }
        if(playList.size() > 1 ){
            playList = playList.subList(1, playList.size());
            return true;
        }
        resetPlayList();
        return false;
    }

    public boolean hasMoreTrack(){
        return playList.size() > 1 || isLoopingOnTrack();
    }

    public void addTrack(Track track){
        playList.add(track);
    }

    public void resetPlayList(){
        playList = new ArrayList<>();
    }

    public int getTrackAmount() { return playList.size(); }

    public String getNextTrackURL() {
        if(isLoopingOnTrack()){
            return playList.get(0).getVideoUrl();
        }
        else if(playList.size() > 1){
            return playList.get(1).getVideoUrl();
        }
        return NO_MORE_SOUND_QUEUED;
    }

    public List<Track> getTrackList() {
        return this.playList;
    }

}
