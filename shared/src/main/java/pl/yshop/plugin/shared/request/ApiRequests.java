package pl.yshop.plugin.shared.request;

import com.google.gson.Gson;
import okhttp3.*;
import pl.yshop.plugin.shared.exceptions.RequestException;
import pl.yshop.plugin.shared.objects.CommandEntity;
import pl.yshop.plugin.shared.configuration.PluginConfiguration;
import pl.yshop.plugin.shared.objects.ErrorEntity;
import java.util.List;

public class ApiRequests {
    private OkHttpClient client;
    private Gson gson = new Gson();
    private PluginConfiguration configuration;
    private String baseUrl;

    public ApiRequests(PluginConfiguration configuration, OkHttpClient client){
        this.configuration = configuration;
        this.baseUrl = "https://api-v4.yshop.pl/shops/%s/payments/commands/%s/";
        this.client = client;
    }

    public List<CommandEntity> getCommandsToExecute() throws RequestException{
        try {
            Response response = this.client.newCall(this.prepareRequest(this.buildUrl(null), "GET")).execute();
            this.catchErrors(response);
            return List.of(this.gson.fromJson(response.body().string(), CommandEntity[].class));
        } catch (Exception exception) {
            throw new RequestException(exception.getMessage());
        }
    }
    public void confirmTransaction(String id) throws RequestException{
        try {
            Response response = this.client.newCall(this.prepareRequest(this.buildUrl(id), "PUT")).execute();
            this.catchErrors(response);
        } catch (Exception exception) {
            throw new RequestException(exception.getMessage());
        }
    }

    private String buildUrl(String id){
        String url = String.format(this.baseUrl, this.configuration.getShopId(), this.configuration.getServerId());
        return id == null ? url : url+id;
    }

    private Request prepareRequest(String url, String method){
        return new Request.Builder().url(url)
                .method(method, method == "GET" ? null : RequestBody.create("", MediaType.parse("application/json; charset=utf-8")))
                .header("User-Agent", "yShopPlugin/1.0")
                .addHeader("X-API-KEY", this.configuration.getApikey())
                .build();
    }

    private void catchErrors(Response response) throws RequestException{
        try {
            if(response.code() == 502) throw new RequestException("Serwer API nie odpowiada!");
            if(!response.isSuccessful()){
                ErrorEntity error = this.gson.fromJson(response.body().string(), ErrorEntity.class);
                throw new RequestException(String.format("Serwer zwrocil kod http: %d. Tresc bledu: %s", error.getStatusCode(), error.getMessage()));
            }
        }catch (Exception exception){
            throw new RequestException(exception.getMessage());
        }
    }
}