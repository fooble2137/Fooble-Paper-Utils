package dev.fooble.mc.worlds.api;

import java.util.Collection;

public interface WorldManager {

    void loadDefaultWorlds();

    ManagedWorld createWorld(String name, WorldOptions options);

    ManagedWorld getWorld(String name);

    Collection<ManagedWorld> getWorlds();

    Collection<ManagedWorld> getWorlds(WorldType type);

    void deleteWorld(String name);
}