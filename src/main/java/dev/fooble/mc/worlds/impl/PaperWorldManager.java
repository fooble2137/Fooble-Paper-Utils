package dev.fooble.mc.worlds.impl;

import dev.fooble.mc.worlds.api.ManagedWorld;
import dev.fooble.mc.worlds.api.WorldManager;
import dev.fooble.mc.worlds.api.WorldOptions;
import dev.fooble.mc.worlds.api.WorldType;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.*;

public class PaperWorldManager implements WorldManager {

    private final Map<String, ManagedWorld> worlds = new HashMap<>();

    @Override
    public void loadDefaultWorlds() {
        for(World world : Bukkit.getWorlds()) {
            worlds.putIfAbsent(
                    world.getName(),
                    new DefaultManagedWorld(world)
            );
        }
    }

    @Override
    public ManagedWorld createWorld(String name, WorldOptions options) {
        if(options.getType() == WorldType.DEFAULT) {
            throw new IllegalArgumentException("Cannot create DEFAULT worlds.");
        }

        if(worlds.containsKey(name)) {
            throw new IllegalStateException("World already exists: " + name);
        }

        ManagedWorld managedWorld =
                options.getType() == WorldType.TEMPORARY
                        ? new TemporaryManagedWorld(name, options)
                        : new CustomManagedWorld(name, options);

        managedWorld.load();
        worlds.put(name, managedWorld);
        return managedWorld;
    }

    @Override
    public ManagedWorld getWorld(String name) {
        return worlds.get(name);
    }

    @Override
    public Collection<ManagedWorld> getWorlds() {
        return Collections.unmodifiableCollection(worlds.values());
    }

    @Override
    public Collection<ManagedWorld> getWorlds(WorldType type) {
        return worlds.values().stream()
                .filter(world -> world.getType() == type)
                .toList();
    }

    @Override
    public void deleteWorld(String name) {
        final ManagedWorld world = worlds.get(name);
        if(world == null) return;

        if(!world.isDeletable()) {
            throw new IllegalStateException("World cannot be deleted: " + name);
        }

        world.delete();
        worlds.remove(name);
    }
}