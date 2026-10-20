package dev.fooble.mc.worlds.api;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

@Getter
@Builder
public class WorldOptions {

    @Builder.Default
    private long seed = new Random().nextLong();

    @Builder.Default
    private World.Environment environment = World.Environment.NORMAL;

    private WorldType type;

    private ChunkGenerator chunkGenerator;

    @Builder.Default
    private boolean autoSave = true;

    @Builder.Default
    private boolean allowAnimals = true;

    @Builder.Default
    private boolean allowMonsters = true;
}