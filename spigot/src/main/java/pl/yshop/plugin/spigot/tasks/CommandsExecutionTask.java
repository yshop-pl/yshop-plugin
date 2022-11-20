package pl.yshop.plugin.spigot.tasks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import pl.yshop.plugin.shared.ApiRequests;
import pl.yshop.plugin.shared.ShopExecutionTask;

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

    //    @Override
//    public List<CommandEntity> commandsToExecute() {
//        return this.apiRequests.getCommandsToExecute();
//    }

    @Override
    public boolean isPlayerOnline(String nickname) {
        return this.plugin.getServer().getPlayer(nickname) != null;
    }

    @Override
    public void executeCommand(String command) {
        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }
}