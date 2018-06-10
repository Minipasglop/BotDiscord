package discord.bot.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

public class CustomAudioLoadResultHandler implements AudioLoadResultHandler {

    private AudioPlayer player;
    private AudioManager guildAudioManager;
    private TextChannel chanToWrite;
    private VoiceChannel targetVoicelChannel;

    private final String SOUND_NOT_FOUND = "Le son n'a pas été trouvé.";
    private final String SOUND_PLAY_FAILED = "Je n'ai pas réussi à jouer le son. Peut-être qu'une météorite s'est abattue sur mon micro qui sait ?";

    public void setTargetVoicelChannel(VoiceChannel targetVoicelChannel) {
        this.targetVoicelChannel = targetVoicelChannel;
    }

    public void setChanToWrite(TextChannel chanToWrite) {
        this.chanToWrite = chanToWrite;
    }

    public CustomAudioLoadResultHandler(AudioManager guildAudioManager, AudioPlayer player) {
        this.guildAudioManager = guildAudioManager;
        this.player = player;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        AudioPlayerSendHandler handler = new AudioPlayerSendHandler(player);
        guildAudioManager.setSendingHandler(handler);
        guildAudioManager.openAudioConnection(targetVoicelChannel);
        player.playTrack(audioTrack);
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {

    }

    @Override
    public void noMatches() {
        if(chanToWrite != null) {
            this.chanToWrite.sendMessage(SOUND_NOT_FOUND).queue();
        }
    }

    @Override
    public void loadFailed(FriendlyException e) {
        e.printStackTrace();
        if(chanToWrite != null) {
            this.chanToWrite.sendMessage(SOUND_PLAY_FAILED).queue();
        }
    }
}
