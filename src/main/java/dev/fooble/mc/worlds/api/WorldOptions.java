package dev.fooble.mc.worlds.api;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class WorldOptions {

    private final long seed;

    private final World.Environment environment;

    private final WorldType type;

    private final ChunkGenerator chunkGenerator;

    private final boolean autoSave;

    private final boolean allowAnimals;

    private final boolean allowMonsters;

    private WorldOptions(Builder builder) {
        this.seed = builder.seed;
        this.environment = builder.environment;
        this.type = builder.type;
        this.chunkGenerator = builder.chunkGenerator;
        this.autoSave = builder.autoSave;
        this.allowAnimals = builder.allowAnimals;
        this.allowMonsters = builder.allowMonsters;
    }

    public static Builder builder() {
        return new Builder();
    }

    public long getSeed() {
        return seed;
    }

    public World.Environment getEnvironment() {
        return environment;
    }

    public WorldType getType() {
        return type;
    }

    public ChunkGenerator getChunkGenerator() {
        return chunkGenerator;
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public boolean isAllowAnimals() {
        return allowAnimals;
    }

    public boolean isAllowMonsters() {
        return allowMonsters;
    }

    public static final class Builder {
        private long seed = new Random().nextLong();
        private World.Environment environment = World.Environment.NORMAL;
        private WorldType type;
        private ChunkGenerator chunkGenerator;
        private boolean autoSave = true;
        private boolean allowAnimals = true;
        private boolean allowMonsters = true;

        public Builder seed(long seed) {
            this.seed = seed;
            return this;
        }

        public Builder environment(World.Environment environment) {
            this.environment = environment;
            return this;
        }

        public Builder type(WorldType type) {
            this.type = type;
            return this;
        }

        public Builder chunkGenerator(ChunkGenerator chunkGenerator) {
            this.chunkGenerator = chunkGenerator;
            return this;
        }

        public Builder autoSave(boolean autoSave) {
            this.autoSave = autoSave;
            return this;
        }

        public Builder allowAnimals(boolean allowAnimals) {
            this.allowAnimals = allowAnimals;
            return this;
        }

        public Builder allowMonsters(boolean allowMonsters) {
            this.allowMonsters = allowMonsters;
            return this;
        }

        public WorldOptions build() {
            return new WorldOptions(this);
        }
    }
}