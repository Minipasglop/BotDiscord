package discord.bot.utils.misc;

import discord.bot.BotGlobalManager;



public class CatApi {

    private final String API_URL = "https://api.thecatapi.com/v1/images/search";
    private final String API_KEY_HEADER_NAME = "x-api-key";
    private final String catApiKey;
    private static CatApi instance;
    private final String REQUEST_METHOD = "GET";


    public static CatApi getInstance(){
        if(instance == null){
            instance = new CatApi();
        }
        return instance;
    }

    private CatApi(){
        this.catApiKey = BotGlobalManager.getConfig().getCatApiKey();
    }


    public String getRandomCatImage(){
        return ApiCall.call(API_URL, REQUEST_METHOD, API_KEY_HEADER_NAME, catApiKey);
    }
}
