package pl.yshop.plugin.shared.tasks;

import pl.yshop.plugin.shared.ApiRequests;
import pl.yshop.plugin.shared.entities.CommandEntity;
import pl.yshop.plugin.shared.enums.LogLevel;
import pl.yshop.plugin.shared.exceptions.RequestException;

import java.util.List;

public abstract class ExecuteCommandsTask implements Runnable {
    public abstract ApiRequests apiRequests();
    public abstract void executeCommand(String command);
    public abstract void announce(String message);
    public abstract boolean isPlayerOnline(String nickname);
    public abstract void log(LogLevel level, String message);

    @Override
    public void run() {
        try {
            List<CommandEntity> commandEntities = this.apiRequests().getCommandsToExecute();
            commandEntities.forEach(commandEntity -> {
                if(commandEntity.isRequire_player_online() && !this.isPlayerOnline(commandEntity.getPlayer())) return;
                try {
                    this.apiRequests().confirmTransaction(commandEntity.getId());
                    commandEntity.getCommands().forEach(command -> {
                        if(command.startsWith("announce")) {
                            this.announce(command.replaceFirst("announce",""));
                            return;
                        }
                        this.log(LogLevel.INFO, String.format("Wykonywanie komendy z zamowienia (%s): /%s", commandEntity.getId(), command));
                        this.executeCommand(command);
                    });
                }catch (RequestException exception){
                    this.log(LogLevel.ERROR, "Nie mozna potwierdzic zamowienia " + commandEntity.getId());
                    this.log(LogLevel.ERROR, "Blad: " + exception.getMessage());
                }
            });
        } catch (RequestException exception) {
            this.log(LogLevel.ERROR, "Wystapil blad podczas pobierania listy komend do wykonania!");
            this.log(LogLevel.ERROR, "Blad: " + exception.getMessage());
        }
    }
}