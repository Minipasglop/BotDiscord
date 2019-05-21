package discord.bot.utils.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class ApiCall {

    private static Logger logger = Logger.getLogger(ApiCall.class);


    static String call(String api_url, String request_method, String api_key_header_name, String headerApiKey) {
        String imageUri = "";
        try {
            URL url = new URL(api_url);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod(request_method);
            if(!headerApiKey.isEmpty() && ! api_key_header_name.isEmpty()) {
                request.setRequestProperty(api_key_header_name, headerApiKey);
            }
            request.connect();
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonArray jsonArray = root.getAsJsonArray();
            imageUri = jsonArray.get(0).getAsJsonObject().get("url").getAsString();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            logger.error(e.getMessage());
        }
        return imageUri;
    }
}
