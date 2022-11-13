package pl.yshop.plugin.spigot.tasks;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import pl.yshop.plugin.shared.ApiRequests;
import pl.yshop.plugin.shared.ShopExecutionTask;
import pl.yshop.plugin.shared.objects.CommandEntity;
import pl.yshop.plugin.spigot.SpigotPlugin;

import java.util.List;

public class CommandsExecutionTask extends ShopExecutionTask{
    private ApiRequests apiRequests;
    private Server server;

    public CommandsExecutionTask(Plugin plugin, ApiRequests apiRequests){
        this.apiRequests = apiRequests;
        this.server = plugin.getServer();
    }

    @Override
    public List<CommandEntity> commandsToExecute() {
        return this.apiRequests.getCommandsToExecute();
    }

    @Override
    public boolean isPlayerOnline(String nickname) {
        return this.server.getPlayer(nickname) != null;
    }

    @Override
    public void executeCommand(String command) {

    }
}