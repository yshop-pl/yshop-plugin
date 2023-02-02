package pl.yshop.plugin.shared.configuration;

import lombok.Builder;
import lombok.Getter;
import pl.yshop.plugin.shared.configuration.annotations.NotEmptyValue;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Getter
@Builder
public class PluginConfiguration extends BaseConfiguration {
    @NotEmptyValue(name = "Klucz API")
    private String apikey;

    @NotEmptyValue(name = "Id sklepu")
    private String shopId;

    @NotEmptyValue(name = "Id serwera")
    private String serverId;

    private final Duration taskInterval = Duration.of(30, ChronoUnit.SECONDS);
}
