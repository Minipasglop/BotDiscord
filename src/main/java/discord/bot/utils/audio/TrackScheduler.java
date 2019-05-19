package discord.bot.utils.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private boolean isLooping;

    /**
     * @param player
     *            The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.isLooping = false;
    }

    public void setLooping(boolean looping){
        this.isLooping = looping;
    }

    public void reverseTrackLoop(){
        this.isLooping = !this.isLooping;
    }

    /**
     * Add the next track to queue or play right away if nothing is in the
     * queue.
     *
     * @param track
     *            The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the
        // track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case
        // the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        // Start the next track, regardless of if something is already playing
        // or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply
        // stop the player.
        AudioTrack track = queue.poll();
        player.startTrack(track, false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // loop
        boolean loop = this.isLooping && (endReason == AudioTrackEndReason.FINISHED);
        // save old track
        AudioTrack loopTrack = null;
        if(loop) {
            loopTrack = track.makeClone();
        }
        // Only start the next track if the end reason is suitable for it
        // (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            nextTrack();
        }
        // re add track if loop is enabled
        if (loop) {
            queue(loopTrack);
        }
    }

    public ArrayList<AudioTrack> getTrackList() {
        Iterator<AudioTrack> i = this.queue.iterator();
        ArrayList<AudioTrack> al = new ArrayList<>();
        if(this.player.getPlayingTrack() != null) {
            al.add(this.player.getPlayingTrack());
        }
        while(i.hasNext()) {
            al.add(i.next());
        }
        return al;
    }

    public void stop() {
        this.player.stopTrack();
        this.queue.clear();
    }

    public void shuffle() {
        ArrayList<AudioTrack> list = getTrackList();
        Collections.shuffle(list);
        this.queue.clear();
        for(AudioTrack track : list) {
            this.queue.offer(track);
        }
    }

    public int getTrackAmount(){
        return getTrackList().size();
    }
}