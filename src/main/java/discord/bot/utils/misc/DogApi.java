package discord.bot.utils.misc;

import discord.bot.BotGlobalManager;

public class DogApi {

    private final String API_URL = "https://api.thedogapi.com/v1/images/search";
    private final String API_KEY_HEADER_NAME = "x-api-key";
    private final String dogApiKey;
    private static DogApi instance;
    private final String REQUEST_METHOD = "GET";

    public static DogApi getInstance(){
        if(instance == null){
            instance = new DogApi();
        }
        return instance;
    }

    private DogApi(){
        this.dogApiKey = BotGlobalManager.getConfig().getDogApiKey();
    }

    public String getRandomDogImage(){
        return ApiCall.call(API_URL, REQUEST_METHOD, API_KEY_HEADER_NAME, dogApiKey);
    }
}
