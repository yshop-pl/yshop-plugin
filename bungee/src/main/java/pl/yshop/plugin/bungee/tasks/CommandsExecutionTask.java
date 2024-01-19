package pl.yshop.plugin.bungee.tasks;

import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.plugin.Plugin;
import pl.yshop.plugin.shared.ApiRequests;
import pl.yshop.plugin.shared.enums.LogLevel;
import pl.yshop.plugin.shared.tasks.ExecuteCommandsTask;


public class CommandsExecutionTask extends ExecuteCommandsTask {
    private ApiRequests apiRequests;
    private Plugin plugin;
    private BungeeAudiences audiences;

    public CommandsExecutionTask(Plugin plugin, ApiRequests apiRequests){
        this.apiRequests = apiRequests;
        this.plugin = plugin;
        this.audiences = BungeeAudiences.create(plugin);;
    }
    @Override
    public ApiRequests apiRequests() {
        return this.apiRequests;
    }

    @Override
    public void executeCommand(String command) {
        this.plugin.getProxy().getPluginManager().dispatchCommand(this.plugin.getProxy().getConsole(), command);
    }

    @Override
    public void announce(String message) {
        this.plugin.getProxy().getPlayers().forEach(player -> {
            this.audiences.player(player).sendMessage(MiniMessage.miniMessage().deserialize(message));
        });
    }

    @Override
    public boolean isPlayerOnline(String nickname) {
        return this.plugin.getProxy().getPlayer(nickname) != null;
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
}
