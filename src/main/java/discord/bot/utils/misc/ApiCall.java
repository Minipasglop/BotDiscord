package discord.bot.utils.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class ApiCall {

    private static Logger logger = Logger.getLogger(ApiCall.class);

    static String call(String api_url, String request_method, String api_key_header_name, String headerApiKey, String wantedField) {
        String responseValue = "";
        try {
            URL url = new URL(api_url);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod(request_method);
            if(!headerApiKey.isEmpty() && ! api_key_header_name.isEmpty()) {
                request.setRequestProperty(api_key_header_name, headerApiKey);
            }
            request.setRequestProperty("accept","application/json");
            request.connect();
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonArray jsonArray = root.getAsJsonArray();
            responseValue = jsonArray.get(0).getAsJsonObject().get(wantedField).getAsString();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            logger.error(e.getMessage());
        }
        return responseValue;
    }

    static String callRapidApi(String api_url,String apiKey, String wantedField){
        String responseValue = "";
        try {
            HttpResponse<JsonNode> response = Unirest.get(api_url)
                    .header("X-RapidAPI-Key", apiKey )
                    .header("accept", "application/json")
                    .asJson();
            responseValue = response.getBody().getObject().getString(wantedField);
        } catch (UnirestException e) {
            System.out.println(e.getMessage());
            logger.error(e.getMessage());;
        }
        return responseValue;
    }
}
