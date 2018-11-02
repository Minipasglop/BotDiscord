package discord.bot.utils.misc;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import discord.bot.utils.audio.Track;
import discord.bot.utils.save.PropertiesLoader;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;


public class YoutubeApi {

    private static PropertiesLoader config = new PropertiesLoader();

    private static final long NUMBER_OF_VIDEOS_RETURNED = 1;

    private static YouTube youtube;

    private static Logger logger = Logger.getLogger(YoutubeApi.class);


    public Track searchVideo(String query){
        try {
            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request)  {
                }
            }).setApplicationName("youtube-cmdline-search-jacksonBot").build();

            YouTube.Search.List search = youtube.search().list("id,snippet");

            String apiKey = config.getYoutubeApiKey();
            search.setKey(apiKey);
            search.setQ(query);

            search.setType("video");

            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url,snippet/channelTitle)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null && searchResultList.size() > 0) {
                Track result = new Track(searchResultList.get(0).getId().getVideoId());
                if(searchResultList.get(0).getSnippet() != null) {
                    SearchResultSnippet snippet = searchResultList.get(0).getSnippet();
                    result.setChannelTitle(snippet.getChannelTitle());
                    result.setThumbnailUrl(snippet.getThumbnails().getDefault().getUrl());
                    result.setTitle(snippet.getTitle());
                }
                return result;
            }
            logger.log(Level.INFO,"Track not found with query : " + query);
            return null;
        } catch (GoogleJsonResponseException e) {
            logger.log(Level.ERROR, "There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (IOException e) {
            logger.log(Level.ERROR, "There was a IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            logger.log(Level.ERROR, "The query was : " + query, t);
        }
        return null;
    }

}