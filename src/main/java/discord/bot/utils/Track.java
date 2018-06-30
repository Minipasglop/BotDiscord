package discord.bot.utils;

public class Track {

    private final String YOUTUBE_PREFIX = "https://youtu.be/";

    private String videoUrl;
    private String thumbnailUrl;
    private String title;
    private String channelTitle;

    public Track(String videoUrl) {
        this.videoUrl = YOUTUBE_PREFIX + videoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }
}
