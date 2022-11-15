package pl.yshop.plugin.shared.objects;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Configuration {
    private String apikey;
    private String shopId;
    private String serverId;
    private String server;
}