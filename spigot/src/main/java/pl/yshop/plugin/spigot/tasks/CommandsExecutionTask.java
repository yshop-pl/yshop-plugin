package pl.yshop.plugin.spigot.tasks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import pl.yshop.plugin.shared.request.ApiRequests;
import pl.yshop.plugin.shared.ShopExecutionTask;
import pl.yshop.plugin.shared.enums.LogLevel;

public class CommandsExecutionTask extends ShopExecutionTask{
    private ApiRequests apiRequests;
    private Plugin plugin;

    public CommandsExecutionTask(Plugin plugin, ApiRequests apiRequests){
        this.apiRequests = apiRequests;
        this.plugin = plugin;
    }

    @Override
    public ApiRequests apiRequests() {
        return this.apiRequests;
    }

    @Override
    public void log(LogLevel level, String message) {
        switch (level){
            case ERROR:
                this.plugin.getLogger().severe(message);
                break;
            case WARNING:
                this.plugin.getLogger().warning(message);
                break;
            default:
                this.plugin.getLogger().info(message);
        }
    }

    @Override
    public boolean isPlayerOnline(String nickname) {
        return this.plugin.getServer().getPlayer(nickname) != null;
    }

    @Override
    public void executeCommand(String command) {
        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }
}