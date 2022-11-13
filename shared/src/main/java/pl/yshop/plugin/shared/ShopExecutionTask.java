package pl.yshop.plugin.shared;

import pl.yshop.plugin.shared.objects.CommandEntity;

import java.util.List;

public abstract class ShopExecutionTask implements Runnable {
    public abstract List<CommandEntity> commandsToExecute();
    public abstract void executeCommand(String command);
    public abstract boolean isPlayerOnline(String nickname);

    @Override
    public void run(){
        this.commandsToExecute().forEach(commandEntity -> {
            commandEntity.getCommands().forEach(command -> {
                if(command != null){
                    if(commandEntity.isRequire_player_online()){

                    }
                }
            });
        });
    }
}
