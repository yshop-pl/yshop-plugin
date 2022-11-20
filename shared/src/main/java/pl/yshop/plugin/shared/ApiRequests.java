package pl.yshop.plugin.shared;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.yshop.plugin.shared.exceptions.RequestException;
import pl.yshop.plugin.shared.objects.CommandEntity;
import pl.yshop.plugin.shared.configuration.Configuration;
import pl.yshop.plugin.shared.objects.ErrorEntity;

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

    public List<CommandEntity> getCommandsToExecute() throws RequestException{
        Request request = new Request.Builder()
                .url("https://api-v4.yshop.pl/shops/4/payments/commands/1").method("GET", null)
                .addHeader("X-API-KEY", "ee")
                .build();
        try {
            Response response = this.client.newCall(request).execute();
            if(!response.isSuccessful()){
                ErrorEntity error = this.gson.fromJson(response.body().string(), ErrorEntity.class);
                throw new RequestException(String.format("Serwer zwrocil kod http: %d. Tresc bledu: %s", error.getStatusCode(), error.getMessage()));
            }
            return List.of(this.gson.fromJson(response.body().string(), CommandEntity[].class));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String buildUrl(){
        return String.format(this.baseUrl, this.shopId, this.serverId);
    }
}