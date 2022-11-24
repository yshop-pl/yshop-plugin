package pl.yshop.plugin.spigot;

import okhttp3.OkHttpClient;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.yshop.plugin.shared.request.ApiRequests;
import pl.yshop.plugin.shared.configuration.Configuration;
import pl.yshop.plugin.shared.exceptions.EmptyFieldInConfigurationException;
import pl.yshop.plugin.spigot.tasks.CommandsExecutionTask;

public final class SpigotPlugin extends JavaPlugin {
    private OkHttpClient httpClient = new OkHttpClient();
    private ApiRequests api;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        long checkEvery = 30 * 20;
        Configuration configuration = Configuration.builder()
                .apikey(this.getConfig().getString("apikey"))
                .serverId(this.getConfig().getString("serverId"))
                .shopId(this.getConfig().getString("shopId")).build();

        try {
            configuration.validate();
        }catch (EmptyFieldInConfigurationException exception){
            this.getLogger().severe(exception.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }

        this.api = new ApiRequests(configuration, this.httpClient);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new CommandsExecutionTask(this, api), checkEvery, checkEvery);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        this.httpClient.dispatcher().executorService().shutdown();
    }
}