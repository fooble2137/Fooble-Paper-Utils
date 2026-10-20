package dev.fooble.mc.worlds.impl;

import dev.fooble.mc.worlds.api.AbstractManagedWorld;
import dev.fooble.mc.worlds.api.WorldOptions;
import dev.fooble.mc.worlds.api.WorldType;
import org.bukkit.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class CustomManagedWorld extends AbstractManagedWorld {

    public CustomManagedWorld(String name, WorldOptions options) {
        super(name, options);
    }

    @Override
    public WorldType getType() {
        return WorldType.CUSTOM;
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public boolean isDeletable() {
        return true;
    }

    @Override
    public void load() {
        WorldCreator creator = new WorldCreator(name)
                .seed(options.getSeed())
                .environment(options.getEnvironment());

        if(options.getChunkGenerator() != null) {
            creator.generator(options.getChunkGenerator());
        }

        world = creator.createWorld();
        applyOptions();
    }

    protected void applyOptions() {
        world.setAutoSave(options.isAutoSave());

        world.setAllowMonsterSpawning(options.isAllowMonsters());

        if(!options.isAllowMonsters() && !options.isAllowAnimals()) {
            world.setGameRule(GameRules.SPAWN_MOBS, false);
        }
    }

    @Override
    public void unload(boolean save) {
        if(world != null) {
            Bukkit.unloadWorld(world, save);
            world = null;
        }
    }

    @Override
    public void delete() {
        World world = Bukkit.getWorld(name);
        File worldFolder = world != null ? world.getWorldFolder() : null;

        if((world != null && !Bukkit.unloadWorld(world, false)) || (worldFolder != null && !deleteDirectory(worldFolder))) {
            throw new IllegalStateException(
                    "Could not unload dimension: " + name
            );
        }
    }

    private boolean deleteDirectory(File path) {
    if (path.exists()) {
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
    }
    return path.delete();
}
}
