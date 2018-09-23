package discord.bot.utils.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord.bot.BotGlobalManager;

import java.util.List;

public class AudioServerManager {

    private CustomAudioLoadResultHandler audioLoadResultHandler;
    private CustomAudioPlaylist playlist;
    private AudioPlayer player;
    private AudioPlayerManager audioPlayerManager;
    private Boolean isPlaying;
    private Boolean forceSkip;

    public AudioServerManager(CustomAudioLoadResultHandler audioLoadResultHandler) {
        this.audioLoadResultHandler = audioLoadResultHandler;
        this.audioPlayerManager = BotGlobalManager.getAudioPlayerManager();
        this.player = this.audioPlayerManager.createPlayer();
        this.audioLoadResultHandler.setPlayer(this.player);
        this.isPlaying = false;
        this.forceSkip = false;
    }

    private void loadNextTrack(){
        audioPlayerManager.loadItem(playlist.getCurrentTrackURL(),this.audioLoadResultHandler);
    }

    public Boolean isTrackPaused(){ return player.isPaused(); }

    public void setTrackPaused(boolean paused){
        player.setPaused(paused);
    }

    //Méthode en interface, permettant de tout déléguer au handlePlaylist (On pars du principe qu'il joue un son), et nous dit si on peut skip ou non pour l'interaction avec l'utilisateur
    public boolean skipTrack(){
        isPlaying = false;
        forceSkip = true;
        return playlist.hasMoreTrack();
    }

    public void emptyPlaylist(){
        playlist.resetPlayList();
        player.stopTrack();
    }

    public void loadTrack(Track track) {
        if(playlist == null){
            playlist = new CustomAudioPlaylist(track);
        }
        else {
            playlist.addTrack(track);
        }
        if (player.getPlayingTrack() == null) {
            loadNextTrack();
            new SoundPlaying(this).execute();
        }
    }

    public void reverseTrackLoop(){
        this.playlist.loopOnTrack();
    }

    public boolean isTrackLooping() {
        if(playlist == null) return false;
        return this.playlist.isLoopingOnTrack();
    }

    private void trackEnded( ){
        if(playlist.hasMoreTrack() && (!isTrackLooping() || forceSkip)){
            playlist.skipTrack();
            loadNextTrack();
            new SoundPlaying(this).execute();
        }else if(playlist.hasMoreTrack() && (this.playlist.isLoopingOnTrack() && !forceSkip)){
            loadNextTrack();
            new SoundPlaying(this).execute();
        }else {
            emptyPlaylist();
        }
        forceSkip = false;
    }

    public void handlePlaylist() {
        isPlaying = true;
        boolean hasPlayedTrack = false;
        while(isPlaying){
            if(player.getPlayingTrack() == null && !hasPlayedTrack) continue;
            else {
                hasPlayedTrack = true;
            }
            if(player.getPlayingTrack() == null){
                isPlaying = false;
            }
        }
        trackEnded();
    }

    public void setVolume(int volume){
        player.setVolume(volume);
    }

    public CustomAudioLoadResultHandler getAudioLoadResultHandler() {
        return audioLoadResultHandler;
    }

    public int getTrackAmount() {
        if(playlist == null) return 0;
        return playlist.getTrackAmount();
    }

    public String getNextTrackURL(){ return this.playlist.getNextTrackURL(); }

    public List<Track> getTrackList() { return this.playlist.getTrackList(); }

    public void shufflePlaylist() { this.playlist.shuffle();
    }
}
