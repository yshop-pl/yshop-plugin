package pl.yshop.plugin.shared.configuration;

import pl.yshop.plugin.shared.configuration.annotations.NotEmptyValue;
import pl.yshop.plugin.shared.exceptions.EmptyFieldInConfigurationException;

import java.lang.reflect.Field;

public class BaseConfiguration {
    public void validate() throws EmptyFieldInConfigurationException {
        try {
            for (Field field : this.getClass().getDeclaredFields()){
                field.setAccessible(true);
                if(field.isAnnotationPresent(NotEmptyValue.class)){
                    String value = field.get(this).toString();
                    if(value.isEmpty()) {
                        throw new EmptyFieldInConfigurationException(field.getAnnotation(NotEmptyValue.class).name(), field.getName());
                    }
                }
            }
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
}
