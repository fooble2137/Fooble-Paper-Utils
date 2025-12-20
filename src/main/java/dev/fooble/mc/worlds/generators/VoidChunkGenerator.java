package dev.fooble.mc.worlds.generators;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class VoidChunkGenerator extends ChunkGenerator {

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
        return createChunkData(world);
    }
}
