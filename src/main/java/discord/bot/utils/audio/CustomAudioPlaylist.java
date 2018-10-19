package discord.bot.utils.audio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomAudioPlaylist {

    private List<Track> playList;
    private final String NO_MORE_SOUND_QUEUED = "No more sound queued.";
    private Boolean isLooping;

    public CustomAudioPlaylist(Track track) {
        playList = new ArrayList<>();
        playList.add(track);
        isLooping = false;
    }

    public void loopOnTrack(){ isLooping = !isLooping; }

    public boolean isLoopingOnTrack() { return this.isLooping; }

    public String getCurrentTrackURL(){
        return playList.get(0).getVideoUrl();
    }

    public boolean skipTrack(){
        if(playList.size() > 1 ){
            playList = playList.subList(1, playList.size());
            return true;
        }
        resetPlayList();
        return false;
    }

    public boolean hasMoreTrack(boolean skipLooping){
        if(skipLooping){
            return playList.size() > 1;
        }else {
            return playList.size() > 1 || (playList.size() == 1 && isLooping);
        }
    }

    public void addTrack(Track track){
        playList.add(track);
    }

    public void resetPlayList(){
        playList = new ArrayList<>();
    }

    public int getTrackAmount() { return playList.size(); }

    public String getNextTrackURL() {
        if(playList.size() > 1){
            return playList.get(1).getVideoUrl();
        }
        return NO_MORE_SOUND_QUEUED;
    }

    public List<Track> getTrackList() {
        return this.playList;
    }

    public void shuffle() {
        if(playList.size() > 1) {
            List<Track> shuffled = this.playList.subList(1, this.playList.size());
            Collections.shuffle(shuffled);
            this.playList = this.playList.subList(0,1);
            this.playList.addAll(shuffled);
        }
    }
}
