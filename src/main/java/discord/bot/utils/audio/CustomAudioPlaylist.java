package discord.bot.utils.audio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomAudioPlaylist {

    private List<Track> playList;
    private Track currentTrack;
    private final String NO_MORE_SOUND_QUEUED = "No more sound queued.";
    private Boolean isLooping;

    public CustomAudioPlaylist(Track track) {
        playList = new ArrayList<>();
        currentTrack = track;
        isLooping = false;
    }

    public void loopOnTrack(){ isLooping = !isLooping; }

    public boolean isLoopingOnTrack() { return this.isLooping; }

    public String getCurrentTrackURL(){
        return currentTrack.getVideoUrl();
    }

    public boolean skipTrack(){
        if(playList.size() > 0 ){
            currentTrack = playList.get(0);
            playList = playList.subList(1, playList.size());
            return true;
        }
        resetPlayList();
        return false;
    }

    public boolean hasMoreTrack(boolean skipLooping){
        if(skipLooping){
            return playList.size() > 0;
        }else {
            return playList.size() > 0 || (currentTrack != null && isLooping);
        }
    }

    public void addTrack(Track track){
        if(currentTrack == null){
            currentTrack = track;
        }else {
            playList.add(track);
        }
    }

    public void resetPlayList(){
        playList = new ArrayList<>();
        currentTrack = null;
    }

    public int getTrackAmount() {
        return currentTrack == null ? playList.size() : playList.size() + 1; }

    public String getNextTrackURL() {
        if(playList.size() > 0){
            return playList.get(0).getVideoUrl();
        }
        return NO_MORE_SOUND_QUEUED;
    }

    public List<Track> getTrackList() {
        List<Track> trackList = new ArrayList<>();
        trackList.add(this.currentTrack);
        trackList.addAll(this.playList);
        return trackList;
    }

    public void shuffle() {
        if(playList.size() > 2) {
            List<Track> shuffled = this.playList;
            Collections.shuffle(shuffled);
            this.playList = shuffled;
        }
    }
}
