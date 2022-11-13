package pl.yshop.plugin.spigot;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pl.yshop.plugin.shared.ApiRequests;
import pl.yshop.plugin.spigot.tasks.CommandsExecutionTask;

public final class SpigotPlugin extends JavaPlugin {
    private ApiRequests api;
    @Override
    public void onEnable() {
        long checkEvery = 30 * 20;
        this.api = new ApiRequests("", "3", "4");
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new CommandsExecutionTask(this), checkEvery, checkEvery);
    }

    @Override
    public void onDisable() {

    }

    public ApiRequests getApi() {
        return this.api;
    }
}