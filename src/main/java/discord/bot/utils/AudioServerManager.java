package discord.bot.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord.bot.BotGlobalManager;

public class AudioServerManager {

    private CustomAudioLoadResultHandler audioLoadResultHandler;
    private CustomAudioPlaylist playlist;
    private AudioPlayer player;
    private AudioPlayerManager audioPlayerManager;
    private Boolean isPlaying;
    private Boolean forceSkipping;

    public AudioServerManager(CustomAudioLoadResultHandler audioLoadResultHandler) {
        this.audioLoadResultHandler = audioLoadResultHandler;
        this.audioPlayerManager = BotGlobalManager.getAudioPlayerManager();
        this.player = this.audioPlayerManager.createPlayer();
        this.audioLoadResultHandler.setPlayer(this.player);
        this.isPlaying = false;
        this.forceSkipping = false;
    }

    private void loadNextTrack(){
        audioPlayerManager.loadItem(playlist.getCurrentTrackURL(),this.audioLoadResultHandler);
    }

    //Méthode en interface, permettant de tout déléguer au handlePlaylist (On pars du principe qu'il joue un son), et nous dit si on peut skip ou non pour l'interaction avec l'utilisateur
    public boolean skipTrack(){
        isPlaying = false;
        if(playlist.hasMoreTrack()){
            forceSkipping = true;
        }
        return playlist.hasMoreTrack();
    }

    public void emptyPlaylist(){
        playlist.resetPlayList();
        player.stopTrack();
    }

    public void loadTrack(String track) {
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
        this.playlist.loopOnTrack(!playlist.isLoopingOnTrack());
    }

    public boolean isTrackLooping() { return this.playlist.isLoopingOnTrack(); }

    private void trackEnded( ){
        if(playlist.hasMoreTrack() || playlist.isLoopingOnTrack()){
            playlist.skipTrack(forceSkipping);
            loadNextTrack();
            new SoundPlaying(this).execute();
        }else {
            emptyPlaylist();
        }
        forceSkipping = false;
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

    public Boolean isBotPlaying() {
        return isPlaying;
    }

    public int getTrackAmount() { return playlist.getTrackAmount(); }

    public String getNextTrackURL(){ return this.playlist.getNextTrackURL(); }
}
