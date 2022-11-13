package pl.yshop.plugin.shared;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.yshop.plugin.shared.objects.CommandEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiRequests {
    private static final String API_COMMANDS_URL = "http://localhost:3000/shops/%s/payments/commands/%s";
    private OkHttpClient client;
    private Gson gson = new Gson();
    private String apikey;
    private String serverId;
    private String shopId;

    public ApiRequests(String apikey, String serverId, String shopId){
        this.apikey = apikey;
        this.serverId = serverId;
        this.shopId = shopId;
        this.client = new OkHttpClient();
    }

    public List<CommandEntity> getCommandsToExecute(){
        Request request = new Request.Builder()
                .url(this.buildUrl()).method("GET", null)
                .addHeader("X-API-KEY", this.apikey)
                .build();
        try {
            Response response = this.client.newCall(request).execute();
            List<CommandEntity> commands = List.of(this.gson.fromJson(response.body().string(), CommandEntity[].class));
            return commands;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildUrl(){
        return String.format(API_COMMANDS_URL, this.shopId, this.serverId);
    }
}
