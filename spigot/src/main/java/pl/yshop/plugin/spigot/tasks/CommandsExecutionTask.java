package pl.yshop.plugin.spigot.tasks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import pl.yshop.plugin.shared.ApiRequests;
import pl.yshop.plugin.shared.enums.LogLevel;
import pl.yshop.plugin.shared.tasks.ExecuteCommandsTask;

public class CommandsExecutionTask extends ExecuteCommandsTask {
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