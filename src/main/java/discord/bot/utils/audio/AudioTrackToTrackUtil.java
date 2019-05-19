package discord.bot.utils.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class AudioTrackToTrackUtil {

    public static Track convert(AudioTrack track){
        Track trackToReturn = new Track(track.getInfo().uri);
        trackToReturn.setChannelTitle(track.getInfo().author);
        trackToReturn.setTitle(track.getInfo().title);
        return trackToReturn;
    }
}
