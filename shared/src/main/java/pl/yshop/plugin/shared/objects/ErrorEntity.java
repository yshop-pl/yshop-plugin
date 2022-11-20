package pl.yshop.plugin.shared.objects;

import lombok.Getter;

@Getter
public class ErrorEntity {
    private int statusCode;
    private String message;
    private String error;
}
