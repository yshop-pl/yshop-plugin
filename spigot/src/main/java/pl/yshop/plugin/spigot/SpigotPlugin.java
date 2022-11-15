package pl.yshop.plugin.spigot;

import okhttp3.OkHttpClient;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.yshop.plugin.shared.ApiRequests;
import pl.yshop.plugin.spigot.tasks.CommandsExecutionTask;

public final class SpigotPlugin extends JavaPlugin {
    private OkHttpClient httpClient = new OkHttpClient();
    private ApiRequests api;

    @Override
    public void onEnable() {
        long checkEvery = 30 * 20;
        FileConfiguration configuration = this.getConfig();
        this.api = new ApiRequests(configuration.getString("apikey"), configuration.getString("serverId"), configuration.getString("shopId"), this.httpClient);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new CommandsExecutionTask(this, api), checkEvery, checkEvery);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        this.httpClient.dispatcher().executorService().shutdown();
    }
}