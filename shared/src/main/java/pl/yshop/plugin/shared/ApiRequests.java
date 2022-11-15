package pl.yshop.plugin.shared;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.yshop.plugin.shared.objects.CommandEntity;
import pl.yshop.plugin.shared.objects.Configuration;

import java.io.IOException;
import java.util.List;

public class ApiRequests {
    private OkHttpClient client;
    private Gson gson = new Gson();
    private String apikey;
    private String serverId;
    private String shopId;
    private String baseUrl;

    public ApiRequests(Configuration configuration, OkHttpClient client){
        this.apikey = configuration.getApikey();
        this.serverId = configuration.getServerId();
        this.shopId = configuration.getShopId();
        this.baseUrl = configuration.getServer() + "shops/%s/payments/commands/%s";
        this.client = client;
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
        return String.format(this.baseUrl, this.shopId, this.serverId);
    }
}