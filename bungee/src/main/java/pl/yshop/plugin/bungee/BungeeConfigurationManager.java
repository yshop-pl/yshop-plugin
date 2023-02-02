package pl.yshop.plugin.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class BungeeConfigurationManager {
    private static final String FILE_NAME = "config.yml";
    private Plugin plugin;

    public BungeeConfigurationManager(Plugin plugin){
        this.plugin = plugin;
    }

    public Configuration getConfiguration(){
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.plugin.getDataFolder(), FILE_NAME));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    public void loadConfigurationFile(){
        if(!this.plugin.getDataFolder().exists()) this.plugin.getDataFolder().mkdir();
        File file = new File(this.plugin.getDataFolder(), FILE_NAME);

        if (!file.exists()) {
            try (InputStream inputStream = this.plugin.getResourceAsStream(FILE_NAME)) {
                Files.copy(inputStream, file.toPath());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}