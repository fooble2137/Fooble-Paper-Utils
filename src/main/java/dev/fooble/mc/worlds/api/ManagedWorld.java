package dev.fooble.mc.worlds.api;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface ManagedWorld {

    String getName();

    UUID getUniqueId();

    WorldType getType();

    WorldOptions getOptions();

    World getBukkitWorld();

    boolean isEditable();

    boolean isDeletable();

    void load();

    void unload(boolean save);

    void delete();

    void teleport(Player player);
}