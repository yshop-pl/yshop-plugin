package pl.yshop.plugin.shared.exceptions;

public class EmptyFieldInConfigurationException extends Exception{
    public EmptyFieldInConfigurationException(String name, String fieldName){
        super(String.format("Musisz uzupelnic %s (pole %s) w pliku konfiguracyjnym aby plugin zostal poprawnie zaladowany!", name, fieldName));
    }
}