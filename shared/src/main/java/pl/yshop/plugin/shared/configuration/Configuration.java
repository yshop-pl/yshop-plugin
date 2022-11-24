package pl.yshop.plugin.shared.configuration;

import lombok.Builder;
import lombok.Getter;
import pl.yshop.plugin.shared.configuration.annotations.NotEmptyValue;
import pl.yshop.plugin.shared.exceptions.EmptyFieldInConfigurationException;

import java.lang.reflect.Field;

@Getter
@Builder
public class Configuration {
    @NotEmptyValue(name = "Klucz API")
    private String apikey;

    @NotEmptyValue(name = "Id sklepu")
    private String shopId;

    @NotEmptyValue(name = "Id serwera")
    private String serverId;

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