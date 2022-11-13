package pl.yshop.plugin.shared.objects;

import lombok.Getter;

import java.util.List;

@Getter
public class CommandEntity {
    private String id;
    private String player;
    private boolean require_player_online;
    private List<String> commands;
}
