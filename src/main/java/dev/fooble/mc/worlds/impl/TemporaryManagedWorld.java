package dev.fooble.mc.worlds.impl;

import dev.fooble.mc.worlds.api.WorldOptions;
import dev.fooble.mc.worlds.api.WorldType;

public final class TemporaryManagedWorld extends CustomManagedWorld {

    public TemporaryManagedWorld(String name, WorldOptions options) {
        super(name, options);
    }

    @Override
    public WorldType getType() {
        return WorldType.TEMPORARY;
    }
}
