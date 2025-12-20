package dev.fooble.mc.worlds.impl;

import dev.fooble.mc.worlds.api.AbstractManagedWorld;
import dev.fooble.mc.worlds.api.WorldOptions;
import dev.fooble.mc.worlds.api.WorldType;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public final class DefaultManagedWorld extends AbstractManagedWorld {


    public DefaultManagedWorld(@NotNull World world) {
        super(
                world.getName(),
                WorldOptions.builder()
                        .type(WorldType.DEFAULT)
                        .build()
        );

        this.world = world;
    }

    @Override
    public WorldType getType() {
        return WorldType.DEFAULT;
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public boolean isDeletable() {
        return false;
    }

    @Override
    public void load() {
    }

    @Override
    public void unload(boolean save) {
        throw new UnsupportedOperationException("Default worlds cannot be unloaded.");
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Default worlds cannot be deleted.");
    }
}