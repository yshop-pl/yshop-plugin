package pl.yshop.plugin.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import okhttp3.OkHttpClient;
import pl.yshop.plugin.bungee.tasks.CommandsExecutionTask;
import pl.yshop.plugin.shared.configuration.PluginConfiguration;
import pl.yshop.plugin.shared.exceptions.EmptyFieldInConfigurationException;
import pl.yshop.plugin.shared.request.ApiRequests;

import java.util.concurrent.TimeUnit;

public final class BungeePlugin extends Plugin {
    private Configuration configuration;
    private OkHttpClient httpClient = new OkHttpClient();
    private ApiRequests api;

    @Override
    public void onEnable() {
        BungeeConfigurationManager bungeeConfigurationManager = new BungeeConfigurationManager(this);
        bungeeConfigurationManager.loadConfigurationFile();
        this.configuration = bungeeConfigurationManager.getConfiguration();

        PluginConfiguration configuration = PluginConfiguration.builder()
                .apikey(this.configuration.getString("apikey"))
                .serverId(this.configuration.getString("serverId"))
                .shopId(this.configuration.getString("shopId")).build();

        try {
            configuration.validate();
        }catch (EmptyFieldInConfigurationException exception){
            this.getLogger().severe(exception.getMessage());
        }

        this.api = new ApiRequests(configuration, this.httpClient);
        this.getProxy().getScheduler().schedule(this, new CommandsExecutionTask(this, this.api), 0L, configuration.getTaskInterval().getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        this.getProxy().getScheduler().cancel(this);
        this.httpClient.dispatcher().executorService().shutdown();
    }
}
