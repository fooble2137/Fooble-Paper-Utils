package dev.fooble.mc.worlds.api;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class AbstractManagedWorld implements ManagedWorld {

    protected final String name;
    protected final WorldOptions options;
    protected World world;

    protected AbstractManagedWorld(String name, WorldOptions options) {
        this.name = name;
        this.options = options;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public WorldOptions getOptions() {
        return options;
    }

    @Override
    public UUID getUniqueId() {
        return world != null ? world.getUID() : null;
    }

    @Override
    public World getBukkitWorld() {
        return world;
    }

    @Override
    public void teleport(Player player) {
        if(world == null) return;
        player.teleport(world.getSpawnLocation());
    }
}