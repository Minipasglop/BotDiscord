package discord.bot.utils.misc;

import discord.bot.BotGlobalManager;

public class DogApiCall {

    private final String API_URL = "https://api.thedogapi.com/v1/images/search";
    private final String WANTED_FIELD = "url";
    private final String API_KEY_HEADER_NAME = "x-api-key";
    private final String apiKey;
    private static DogApiCall instance;
    private final String REQUEST_METHOD = "GET";

    public static DogApiCall getInstance(){
        if(instance == null){
            instance = new DogApiCall();
        }
        return instance;
    }

    private DogApiCall(){
        this.apiKey = BotGlobalManager.getConfig().getDogApiKey();
    }

    public String getRandomDogImage(){
        return ApiCall.call(API_URL, REQUEST_METHOD, API_KEY_HEADER_NAME, apiKey, WANTED_FIELD);
    }
}
