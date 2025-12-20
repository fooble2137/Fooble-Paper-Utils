package dev.fooble.mc.worlds.impl;

import dev.fooble.mc.worlds.api.AbstractManagedWorld;
import dev.fooble.mc.worlds.api.WorldOptions;
import dev.fooble.mc.worlds.api.WorldType;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.jetbrains.annotations.NotNull;

import java.io.File;

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
        world.setSpawnFlags(options.isAllowMonsters(), options.isAllowAnimals());
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
        unload(false);
        deleteWorldFolder(new File(Bukkit.getWorldContainer(), name));
    }

    protected void deleteWorldFolder(@NotNull File folder) {
        if(!folder.exists()) return;

        File[] files = folder.listFiles();
        if(files != null) {
            for(File file : files) {
                deleteWorldFolder(file);
            }
        }
        folder.delete();
    }
}
