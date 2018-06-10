package discord.bot.utils;

import java.util.ArrayList;
import java.util.List;

public class CustomAudioPlaylist {

    private List<String> playList;

    public CustomAudioPlaylist(String track) {
        playList = new ArrayList<>();
        playList.add(track);
    }

    public String playCurrentTrack(){
        return playList.get(0);
    }

    public boolean skipTrack(){
        if(playList.size() > 1 ){
            playList = playList.subList(1, playList.size());
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
}
