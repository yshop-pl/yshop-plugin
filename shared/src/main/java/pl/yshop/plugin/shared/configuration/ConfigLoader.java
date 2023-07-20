package pl.yshop.plugin.shared.configuration;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ConfigLoader {
    private final Class<?> loadingClass;
    private final Path dataDirectory;

    public ConfigLoader(final Class<?> loadingClass, final Path dataDirectory) {
        this.loadingClass = loadingClass;
        this.dataDirectory = dataDirectory;
    }

    public void copyConfig(String resourceName) {
        final Path resourcePath = this.dataDirectory.resolve(resourceName);
        if (Files.exists(resourcePath)) return;
        try (final InputStream in = this.loadingClass.getClassLoader().getResourceAsStream(resourceName)) {
            Files.copy(Objects.requireNonNull(in), resourcePath);
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }
}
