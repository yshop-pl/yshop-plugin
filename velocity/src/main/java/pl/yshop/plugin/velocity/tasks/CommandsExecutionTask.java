package pl.yshop.plugin.velocity.tasks;

import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.slf4j.Logger;
import pl.yshop.plugin.shared.ApiRequests;
import pl.yshop.plugin.shared.enums.LogLevel;
import pl.yshop.plugin.shared.tasks.ExecuteCommandsTask;

public class CommandsExecutionTask extends ExecuteCommandsTask {
    private final ProxyServer proxyServer;
    private final ApiRequests apiRequests;
    private final Logger logger;

    public CommandsExecutionTask(ProxyServer proxyServer, Logger logger, ApiRequests apiRequests){
        this.apiRequests = apiRequests;
        this.proxyServer = proxyServer;
        this.logger = logger;
    }

    @Override
    public ApiRequests apiRequests() {
        return this.apiRequests;
    }

    @Override
    public void executeCommand(String command) {
        this.proxyServer.getCommandManager().executeAsync(this.proxyServer.getConsoleCommandSource(), command);
    }

    @Override
    public void announce(String message) {
        this.proxyServer.getAllPlayers().forEach(player -> {
            player.sendMessage(MiniMessage.miniMessage().deserialize(message));
        });
    }

    @Override
    public boolean isPlayerOnline(String nickname) {
        return this.proxyServer.getPlayer(nickname).isPresent();
    }

    @Override
    public void log(LogLevel level, String message) {
        switch (level) {
            case ERROR:
                this.logger.error(message);
                break;
            case WARNING:
                this.logger.warn(message);
                break;
            default:
                this.logger.info(message);
        }
    }
}
