package pl.yshop.plugin.shared.configuration;

import lombok.Builder;
import lombok.Getter;
import pl.yshop.plugin.shared.configuration.annotations.NotEmptyValue;
import pl.yshop.plugin.shared.exceptions.EmptyFieldInConfigurationException;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Getter
@Builder
public class PluginConfiguration {
    @NotEmptyValue(name = "Klucz API")
    private String apikey;

    @NotEmptyValue(name = "Id sklepu")
    private String shopId;

    @NotEmptyValue(name = "Id serwera")
    private String serverId;

    private final Duration taskInterval = Duration.of(30, ChronoUnit.SECONDS);

    public void validate() throws EmptyFieldInConfigurationException {
        try {
            for (Field field : this.getClass().getDeclaredFields()){
                field.setAccessible(true);
                if(field.isAnnotationPresent(NotEmptyValue.class)){
                    String value = field.get(this).toString();
                    if(value.isEmpty()){
                        throw new EmptyFieldInConfigurationException(field.getAnnotation(NotEmptyValue.class).name(), field.getName());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}