package discord.bot.command.misc;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord.bot.BotGlobalManager;
import discord.bot.command.ICommand;
import discord.bot.utils.audio.GuildMusicManager;
import discord.bot.utils.audio.GuildMusicManagerSupervisor;
import discord.bot.utils.audio.Track;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.misc.YoutubeApi;
import discord.bot.utils.save.PropertyEnum;
import discord.bot.utils.save.ServerPropertiesManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

import java.awt.*;

public class SoundPlayerCommand extends ICommand {

    private final String HELP = "Plays a track from Youtube using the name. \nUsage : `!" + this.commandName + " songName`";
    private final String JOIN_VOCAL_CHANNEL = "Please join a vocal channel.";
    private final String SOUND_QUEUED = "Sound has been queued";
    private final String NO_RESULT = "No result have been found. Make sure you didn't misspelled the track name :wink:";

    private YoutubeApi youtubeApi;

    public SoundPlayerCommand(String commandName){
        super(commandName);
        youtubeApi = BotGlobalManager.getYoutubeApi();
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = GuildMusicManagerSupervisor.getInstance().getGuildMusicManager(guildId);
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return args.length != 0 && !args[0].equals("help");
    }

    private void loadAndPlay(final VoiceChannel voiceChannel, final TextChannel channel, final Track youtubeSearch, final int volume){
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.player.setVolume(volume);
        if(("true").equals(ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(voiceChannel.getGuild().getId(), PropertyEnum.LOOP.getPropertyName()))) {
            musicManager.scheduler.setLooping(true);
        }
        BotGlobalManager.getAudioPlayerManager().loadItemOrdered(musicManager, youtubeSearch.getVideoUrl(), new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                play(channel.getGuild(), musicManager, track, voiceChannel);
                EmbedBuilder builder = new EmbedBuilder();
                builder.setAuthor(SOUND_QUEUED);
                builder.setColor(Color.ORANGE);
                builder.setThumbnail(youtubeSearch.getThumbnailUrl());
                builder.addField("Track Title :musical_note:", youtubeSearch.getTitle() + " - " + youtubeSearch.getChannelTitle() + "\n", false);
                builder.addField("Playlist status :bulb:", musicManager.scheduler.getTrackAmount() + " track" + (musicManager.scheduler.getTrackAmount() == 1 ? "" : "s") + " listed.", false);
                if(channel.canTalk()){
                    MessageSenderFactory.getInstance().sendSafeMessage(channel,builder.build());
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                //Not supported yet
            }

            @Override
            public void noMatches() {
                MessageSenderFactory.getInstance().sendSafeMessage(channel,"Nothing found by " + youtubeSearch.getVideoUrl());
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                MessageSenderFactory.getInstance().sendSafeMessage(channel,"Could not play: " + exception.getMessage());
            }
        });
    }
    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, VoiceChannel voiceChannel) {
        connectToVoiceChannel(guild.getAudioManager(), voiceChannel);

        musicManager.scheduler.queue(track);
    }

    private static void connectToVoiceChannel(AudioManager audioManager, VoiceChannel voiceChannel) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            audioManager.openAudioConnection(voiceChannel);
        }
    }


    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        int volume = 50;
        VoiceChannel targetChannel = event.getMember().getVoiceState().getChannel();
        if (targetChannel == null) {
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),JOIN_VOCAL_CHANNEL);
        } else {
            if(!("").equals(ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(event.getGuild().getId(),"volume")) && ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(event.getGuild().getId(),"volume") != null){
                volume = Integer.parseInt(ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(event.getGuild().getId(),"volume"));
            }
            String youtubeQuery = "";
            for(int i = 0; i  < args.length; ++i){
                youtubeQuery += args[i] + " ";
            }
            System.out.println(event.getAuthor().getName() + " searched : " + youtubeQuery + " on server : " + event.getGuild().getName());
            Track youtubeSearch = youtubeApi.searchVideo(youtubeQuery);
            if (youtubeSearch != null) {
                loadAndPlay(targetChannel, event.getTextChannel(), youtubeSearch, volume);
            }else {
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),NO_RESULT);
            }
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}