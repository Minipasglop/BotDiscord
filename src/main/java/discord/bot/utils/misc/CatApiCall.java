package discord.bot.utils.misc;

import discord.bot.BotGlobalManager;



public class CatApiCall {

    private final String API_URL = "https://api.thecatapi.com/v1/images/search";
    private final String WANTED_FIELD = "url";
    private final String API_KEY_HEADER_NAME = "x-api-key";
    private final String apiKey;
    private static CatApiCall instance;
    private final String REQUEST_METHOD = "GET";


    public static CatApiCall getInstance(){
        if(instance == null){
            instance = new CatApiCall();
        }
        return instance;
    }

    private CatApiCall(){
        this.apiKey = BotGlobalManager.getConfig().getCatApiKey();
    }


    public String getRandomCatImage(){
        return ApiCall.call(API_URL, REQUEST_METHOD, API_KEY_HEADER_NAME, apiKey, WANTED_FIELD);
    }
}
