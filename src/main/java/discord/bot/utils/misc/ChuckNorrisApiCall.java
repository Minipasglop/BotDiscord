package discord.bot.utils.misc;

import discord.bot.BotGlobalManager;

public class ChuckNorrisApiCall {

    private final String API_URL = "https://matchilling-chuck-norris-jokes-v1.p.rapidapi.com/jokes/random";
    private String apiKey;
    private final String WANTED_FIELD = "value";
    private static ChuckNorrisApiCall instance;

    public static ChuckNorrisApiCall getInstance(){
        if(instance == null){
            instance = new ChuckNorrisApiCall();
        }
        return instance;
    }

    private ChuckNorrisApiCall(){
        apiKey = BotGlobalManager.getConfig().getChuckNorrisApiKey();
    }

    public String getRandomFact(){
        return ApiCall.callRapidApi(API_URL, apiKey, WANTED_FIELD);
    }
}
