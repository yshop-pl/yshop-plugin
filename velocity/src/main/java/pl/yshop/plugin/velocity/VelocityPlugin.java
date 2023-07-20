package pl.yshop.plugin.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import pl.yshop.plugin.shared.ApiRequests;
import pl.yshop.plugin.shared.configuration.ConfigLoader;
import pl.yshop.plugin.shared.configuration.PluginConfiguration;
import pl.yshop.plugin.shared.exceptions.EmptyFieldInConfigurationException;
import pl.yshop.plugin.velocity.tasks.CommandsExecutionTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Plugin(
        id = "yshop-plugin",
        name = "yShopV4-connector",
        version = "1.3"
)
public class VelocityPlugin {
    @Inject private Logger logger;
    @Inject private ProxyServer proxyServer;
    @Inject private @DataDirectory Path dataDirectory;
    private ScheduledTask executionTask;
    private final OkHttpClient httpClient = new OkHttpClient();

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        ConfigurationNode node = this.getConfiguration();
        PluginConfiguration configuration = PluginConfiguration.builder()
                .apikey(node.getString("apikey"))
                .serverId(node.getString("serverId"))
                .shopId(node.getString("shopId"))
                .build();

        try {
            configuration.validate();
        } catch (EmptyFieldInConfigurationException exception){
            this.logger.error(exception.getMessage());
        }

        ApiRequests api = new ApiRequests(configuration, this.httpClient);
        this.executionTask = this.proxyServer.getScheduler()
                .buildTask(this, new CommandsExecutionTask(this.proxyServer, this.logger, api))
                .repeat(configuration.getTaskInterval())
                .schedule();
    }
    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (this.executionTask != null) {
            this.executionTask.cancel();
        }
        this.httpClient.dispatcher().executorService().shutdown();
    }

    private ConfigurationNode getConfiguration() {
        File pluginDataFolder = this.dataDirectory.toFile();
        if (!pluginDataFolder.exists() && pluginDataFolder.mkdirs()) {
            this.logger.info("Successfully created data folder.");
        }
        ConfigLoader configLoader = new ConfigLoader(this.getClass(), pluginDataFolder.toPath());
        configLoader.copyConfig("config.yml");
        try {
            return YAMLConfigurationLoader.builder().setPath(Path.of(pluginDataFolder.getPath(), "config.yml")).build().load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
