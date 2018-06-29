package discord.bot.utils;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;

import java.io.IOException;
import java.util.List;


public class YoutubeApi {

    private static PropertiesLoader config = new PropertiesLoader();

    private static final long NUMBER_OF_VIDEOS_RETURNED = 1;


    private static YouTube youtube;


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
            if (searchResultList != null) {
                SearchResultSnippet snippet = searchResultList.get(0).getSnippet();
                Track result = new Track(searchResultList.get(0).getId().getVideoId());
                result.setChannelTitle(snippet.getChannelTitle());
                result.setThumbnailUrl(snippet.getThumbnails().getDefault().getUrl());
                result.setTitle(snippet.getTitle());
                return result;
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

}