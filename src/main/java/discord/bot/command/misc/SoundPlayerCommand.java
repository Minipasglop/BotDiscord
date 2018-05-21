package discord.bot.command.misc;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord.bot.BotGlobalManager;
import discord.bot.command.ICommand;
import discord.bot.utils.YoutubeApi;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;
import discord.bot.utils.AudioPlayerSendHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class SoundPlayerCommand implements ICommand {

    private final String HELP = "Joue le son souhaité dans le salon vocal courant de l'utilisateur. \nUsage : `!sound <nomDuSon>`";

    private final String SOUND_FOLDER = "sounds";

    private final YoutubeApi youtubeApi = BotGlobalManager.getYoutubeApi();


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return args.length != 0 && !args[0].equals("help");
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        File folder = new File(SOUND_FOLDER);
        AudioManager guildAudioManager = event.getGuild().getAudioManager();
        AudioPlayer player = BotGlobalManager.getAudioPlayerManager().createPlayer();

        switch (args[0]) {
            case "liste":
                ArrayList<String> list = new ArrayList<>();
                for (File file : Objects.requireNonNull(folder.listFiles())) {
                    if (file.getName().contains(".mp3")) {
                        list.add(file.getName().replace(".mp3", "").toLowerCase());
                    }
                }

                String fileList = "```Sons disponibles :\n";
                int compteur = 0;
                for (String fileName : list) {
                    compteur++;
                    while (fileName.length() < 15) {
                        fileName += " ";
                    }
                    fileList += fileName + "\t";
                    if (0 == compteur % 6) fileList += "\n";
                    if (0 == compteur % 42) {
                        fileList += "```";
                        event.getTextChannel().sendMessage(fileList).queue();
                        fileList = "```";
                    }
                }
                fileList += "```";
                event.getTextChannel().sendMessage(fileList).queue();

                break;
            case "stop":
                try {
                    guildAudioManager.setSendingHandler(null);
                    guildAudioManager.closeAudioConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "yt":
                VoiceChannel targetChannel = event.getMember().getVoiceState().getChannel();
                if (targetChannel == null) {
                    event.getTextChannel().sendMessage("Veuillez rejoindre un salon vocal.").queue();
                } else {
                    String youtubeQuery = "";
                    for(int i = 1; i  < args.length; ++i){
                        youtubeQuery += args[i] + " ";
                    }
                    String youtubeSearch = youtubeApi.searchVideo(youtubeQuery);
                    if (!("").equalsIgnoreCase(youtubeSearch)) {
                        String videoURL = "https://youtu.be/" + youtubeSearch;
                        BotGlobalManager.getAudioPlayerManager().loadItem(videoURL, new AudioLoadResultHandler() {
                            @Override
                            public void trackLoaded(AudioTrack audioTrack) {
                                AudioPlayerSendHandler handler = new AudioPlayerSendHandler(player);
                                guildAudioManager.setSendingHandler(handler);
                                guildAudioManager.openAudioConnection(targetChannel);
                                player.playTrack(audioTrack);
                            }

                            @Override
                            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                                //unusued
                            }

                            @Override
                            public void noMatches() {
                                event.getTextChannel().sendMessage("Le son n'a pas été trouvé.").queue();
                            }

                            @Override
                            public void loadFailed(FriendlyException e) {
                                e.printStackTrace();
                                event.getTextChannel().sendMessage("Je n'ai pas réussi à jouer le son. Peut-être qu'une météorite s'est abattue sur mon micro qui sait ?").queue();
                            }
                        });
                    }
                }
                break;
            default:
                targetChannel = event.getMember().getVoiceState().getChannel();
                if (targetChannel == null) {
                    event.getTextChannel().sendMessage("Veuillez rejoindre un salon vocal.").queue();
                } else {
                    String trackUrl = "./" + folder.getPath() + "/" + args[0].toLowerCase() + ".mp3";
                    BotGlobalManager.getAudioPlayerManager().loadItem(new File(trackUrl).getPath(), new AudioLoadResultHandler() {
                        @Override
                        public void trackLoaded(AudioTrack audioTrack) {
                            AudioPlayerSendHandler handler = new AudioPlayerSendHandler(player);
                            guildAudioManager.setSendingHandler(handler);
                            guildAudioManager.openAudioConnection(targetChannel);
                            player.playTrack(audioTrack);
                        }

                        @Override
                        public void playlistLoaded(AudioPlaylist audioPlaylist) {
                            //unusued
                        }

                        @Override
                        public void noMatches() {
                            event.getTextChannel().sendMessage("Le son **" + args[0] + "** n'a pas été trouvé.").queue();
                        }

                        @Override
                        public void loadFailed(FriendlyException e) {
                            e.printStackTrace();
                            event.getTextChannel().sendMessage("Je n'ai pas réussi à jouer le son. Peut-être qu'une météorite s'est abattue sur mon micro qui sait ?").queue();
                        }
                    });
                }
                break;
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        if(!success) {
            event.getTextChannel().sendMessage(help()).queue();
        }
    }
}