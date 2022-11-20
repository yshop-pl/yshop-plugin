package pl.yshop.plugin.shared;

import pl.yshop.plugin.shared.exceptions.RequestException;
import pl.yshop.plugin.shared.objects.CommandEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

public abstract class ShopExecutionTask implements Runnable {
//    public abstract List<CommandEntity> commandsToExecute();
    public abstract ApiRequests apiRequests();
    public abstract void executeCommand(String command);
    public abstract boolean isPlayerOnline(String nickname);

    @Override
    public void run(){
        List<CommandEntity> commandEntities = null;

        try {
            commandEntities = this.apiRequests().getCommandsToExecute();
        }catch (RequestException exception){

        }
        if(commandEntities == null) return;
        commandEntities.forEach(commandEntity -> {
            commandEntity.getCommands().forEach(command -> {
                if(commandEntity.isRequire_player_online() && !this.isPlayerOnline(commandEntity.getPlayer())){
                    System.out.println("Gracz " + commandEntity.getPlayer() + " jest offline!");
                    return;
                }
                this.executeCommand(command);
            });
        });
    }
}