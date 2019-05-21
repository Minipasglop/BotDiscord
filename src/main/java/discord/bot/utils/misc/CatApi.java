package discord.bot.utils.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import discord.bot.BotGlobalManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CatApi {

    private final String CAT_API_URI = "https://api.thecatapi.com/v1/images/search";
    private final String API_KEY_HEADER_NAME = "x-api-key";
    private final String catApiKey;
    private static CatApi instance;
    private final String REQUEST_METHOD = "GET";

    private static Logger logger = Logger.getLogger(CatApi.class);


    public static CatApi getInstance(){
        if(instance == null){
            instance = new CatApi();
        }
        return instance;
    }

    private CatApi(){
        this.catApiKey = BotGlobalManager.getConfig().getCatApiKey();
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public String getRandomCatImage(){
        String catImageUri = "";
        try {
            URL url = new URL(CAT_API_URI);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod(REQUEST_METHOD);
            request.setRequestProperty(API_KEY_HEADER_NAME,catApiKey);
            request.connect();
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonArray jsonArray = root.getAsJsonArray();
            catImageUri = jsonArray.get(0).getAsJsonObject().get("url").getAsString();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            logger.error(e.getMessage());
        }
        return catImageUri;
    }
}
