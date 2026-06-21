package dev.fooble.mc.particles;

import org.bukkit.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility helpers for spawning practical particle effects.
 *
 * <p>The class focuses on the most common particle use cases in Paper plugins:
 * one-off spawns, dust helpers, rings, lines, spheres, helixes, and bursts.</p>
 */
public final class ParticleUtils {

    private static final double FULL_ROTATION = Math.PI * 2.0;
    private static final double DEFAULT_SPACING = 0.35D;
    private static final int DEFAULT_CIRCLE_POINTS = 32;
    private static final int DEFAULT_SPHERE_LATITUDE_STEPS = 12;
    private static final int DEFAULT_SPHERE_LONGITUDE_STEPS = 24;
    private static final int DEFAULT_HELIX_TURNS = 3;
    private static final int DEFAULT_HELIX_POINTS_PER_TURN = 40;

    private ParticleUtils() {
    }

    /**
     * Spawns a single particle at the given location.
     *
     * @param location The spawn location.
     * @param particle The particle to spawn.
     */
    public static void spawn(@NotNull final Location location, @NotNull final Particle particle) {
        spawn(location, particle, 1);
    }

    /**
     * Spawns the given particle count at the given location.
     *
     * @param location The spawn location.
     * @param particle The particle to spawn.
     * @param count    The amount of particles to spawn.
     */
    public static void spawn(
            @NotNull final Location location,
            @NotNull final Particle particle,
            final int count
    ) {
        spawn(location, particle, count, 0D, 0D, 0D, 0D);
    }

    /**
     * Spawns a particle with spread and optional data.
     *
     * @param location The spawn location.
     * @param particle The particle to spawn.
     * @param count    The amount of particles to spawn.
     * @param offsetX  The X offset.
     * @param offsetY  The Y offset.
     * @param offsetZ  The Z offset.
     * @param extra    The extra speed/distance parameter.
     * @param data     Optional particle data.
     * @param <T>      Particle data type.
     */
    public static <T> void spawn(
            @NotNull final Location location,
            @NotNull final Particle particle,
            final int count,
            final double offsetX,
            final double offsetY,
            final double offsetZ,
            final double extra,
            @Nullable final T data
    ) {
        final World world = requireWorld(location);

        if(data == null) {
            world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra);
            return;
        }

        world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data);
    }

    /**
     * Spawns a particle with spread.
     *
     * @param location The spawn location.
     * @param particle The particle to spawn.
     * @param count    The amount of particles to spawn.
     * @param offsetX  The X offset.
     * @param offsetY  The Y offset.
     * @param offsetZ  The Z offset.
     * @param extra    The extra speed/distance parameter.
     */
    public static void spawn(
            @NotNull final Location location,
            @NotNull final Particle particle,
            final int count,
            final double offsetX,
            final double offsetY,
            final double offsetZ,
            final double extra
    ) {
        spawn(location, particle, count, offsetX, offsetY, offsetZ, extra, null);
    }

    /**
     * Creates a DustOptions instance for redstone-style particles.
     *
     * @param color The particle color.
     * @param size  The particle size.
     * @return A new DustOptions object.
     */
    public static @NotNull Particle.DustOptions dust(@NotNull final Color color, final float size) {
        return new Particle.DustOptions(color, size);
    }

    /**
     * Creates a DustOptions instance for redstone-style particles.
     *
     * @param color The particle color.
     * @return A new DustOptions object with a default size of 1.0.
     */
    public static @NotNull Particle.DustOptions dust(@NotNull final Color color) {
        return dust(color, 1.0F);
    }

    /**
     * Creates a DustTransition instance for color-shifting redstone-style particles.
     *
     * @param from The starting color.
     * @param to   The ending color.
     * @param size The particle size.
     * @return A new DustTransition object.
     */
    public static @NotNull Particle.DustTransition dustTransition(
            @NotNull final Color from,
            @NotNull final Color to,
            final float size
    ) {
        return new Particle.DustTransition(from, to, size);
    }

    /**
     * Spawns a flat circle on the XZ plane.
     *
     * @param center   The circle center.
     * @param radius   The circle radius.
     * @param particle The particle to spawn.
     * @return The number of particles spawned.
     */
    public static int circle(
            @NotNull final Location center,
            final double radius,
            @NotNull final Particle particle
    ) {
        return circle(center, radius, particle, DEFAULT_CIRCLE_POINTS, Axis.Y, null);
    }

    /**
     * Spawns a flat circle on the XZ plane.
     *
     * @param center   The circle center.
     * @param radius   The circle radius.
     * @param particle The particle to spawn.
     * @param points   The number of points to plot.
     * @return The number of particles spawned.
     */
    public static int circle(
            @NotNull final Location center,
            final double radius,
            @NotNull final Particle particle,
            final int points
    ) {
        return circle(center, radius, particle, points, Axis.Y, null);
    }

    /**
     * Spawns a circle around the chosen axis.
     *
     * @param center   The circle center.
     * @param radius   The circle radius.
     * @param particle The particle to spawn.
     * @param points   The number of points to plot.
     * @param axis     The circle axis.
     * @param data     Optional particle data.
     * @param <T>      Particle data type.
     * @return The number of particles spawned.
     */
    public static <T> int circle(
            @NotNull final Location center,
            final double radius,
            @NotNull final Particle particle,
            final int points,
            @NotNull final Axis axis,
            @Nullable final T data
    ) {
        validateNonNegative(radius, "radius");
        validatePositive(points, "points");

        final World world = requireWorld(center);
        int spawned = 0;

        for(int i = 0; i < points; i++) {
            final double angle = FULL_ROTATION * i / points;
            final Location point = center.clone();

            switch(axis) {
                case X -> point.add(0D, radius * Math.cos(angle), radius * Math.sin(angle));
                case Y -> point.add(radius * Math.cos(angle), 0D, radius * Math.sin(angle));
                case Z -> point.add(radius * Math.cos(angle), radius * Math.sin(angle), 0D);
            }

            spawn(world, point, particle, data);
            spawned++;
        }

        return spawned;
    }

    /**
     * Spawns a circle around the chosen axis.
     *
     * @param center   The circle center.
     * @param radius   The circle radius.
     * @param particle The particle to spawn.
     * @param points   The number of points to plot.
     * @param axis     The circle axis.
     * @return The number of particles spawned.
     */
    public static int circle(
            @NotNull final Location center,
            final double radius,
            @NotNull final Particle particle,
            final int points,
            @NotNull final Axis axis
    ) {
        return circle(center, radius, particle, points, axis, null);
    }

    /**
     * Alias for {@link #circle(Location, double, Particle, int, Axis)}.
     *
     * @param center   The ring center.
     * @param radius   The ring radius.
     * @param particle The particle to spawn.
     * @param points   The number of points to plot.
     * @param axis     The ring axis.
     * @return The number of particles spawned.
     */
    public static int ring(
            @NotNull final Location center,
            final double radius,
            @NotNull final Particle particle,
            final int points,
            @NotNull final Axis axis
    ) {
        return circle(center, radius, particle, points, axis);
    }

    /**
     * Alias for {@link #circle(Location, double, Particle)}.
     *
     * @param center   The ring center.
     * @param radius   The ring radius.
     * @param particle The particle to spawn.
     * @return The number of particles spawned.
     */
    public static int ring(
            @NotNull final Location center,
            final double radius,
            @NotNull final Particle particle
    ) {
        return circle(center, radius, particle);
    }

    /**
     * Spawns a sphere shell around a center point.
     *
     * @param center   The sphere center.
     * @param radius   The sphere radius.
     * @param particle The particle to spawn.
     * @return The number of particles spawned.
     */
    public static int sphere(
            @NotNull final Location center,
            final double radius,
            @NotNull final Particle particle
    ) {
        return sphere(center, radius, particle, DEFAULT_SPHERE_LATITUDE_STEPS, DEFAULT_SPHERE_LONGITUDE_STEPS, null);
    }

    /**
     * Spawns a sphere shell around a center point.
     *
     * @param center         The sphere center.
     * @param radius         The sphere radius.
     * @param particle       The particle to spawn.
     * @param latitudeSteps  The number of latitude slices.
     * @param longitudeSteps The number of longitude slices.
     * @return The number of particles spawned.
     */
    public static int sphere(
            @NotNull final Location center,
            final double radius,
            @NotNull final Particle particle,
            final int latitudeSteps,
            final int longitudeSteps
    ) {
        return sphere(center, radius, particle, latitudeSteps, longitudeSteps, null);
    }

    /**
     * Spawns a sphere shell around a center point.
     *
     * @param center         The sphere center.
     * @param radius         The sphere radius.
     * @param particle       The particle to spawn.
     * @param latitudeSteps  The number of latitude slices.
     * @param longitudeSteps The number of longitude slices.
     * @param data           Optional particle data.
     * @param <T>            Particle data type.
     * @return The number of particles spawned.
     */
    public static <T> int sphere(
            @NotNull final Location center,
            final double radius,
            @NotNull final Particle particle,
            final int latitudeSteps,
            final int longitudeSteps,
            @Nullable final T data
    ) {
        validateNonNegative(radius, "radius");
        validatePositive(latitudeSteps, "latitudeSteps");
        validatePositive(longitudeSteps, "longitudeSteps");

        final World world = requireWorld(center);
        int spawned = 0;

        for(int lat = 0; lat <= latitudeSteps; lat++) {
            final double phi = Math.PI * lat / latitudeSteps;
            final double sinPhi = Math.sin(phi);
            final double cosPhi = Math.cos(phi);

            for(int lon = 0; lon <= longitudeSteps; lon++) {
                final double theta = FULL_ROTATION * lon / longitudeSteps;
                final double x = radius * sinPhi * Math.cos(theta);
                final double y = radius * cosPhi;
                final double z = radius * sinPhi * Math.sin(theta);
                final Location point = center.clone().add(x, y, z);

                spawn(world, point, particle, data);
                spawned++;
            }
        }

        return spawned;
    }

    /**
     * Spawns a line between two locations.
     *
     * @param from     The starting location.
     * @param to       The ending location.
     * @param particle The particle to spawn.
     * @return The number of particles spawned.
     */
    public static int line(
            @NotNull final Location from,
            @NotNull final Location to,
            @NotNull final Particle particle
    ) {
        return line(from, to, particle, DEFAULT_SPACING, null);
    }

    /**
     * Spawns a line between two locations.
     *
     * @param from     The starting location.
     * @param to       The ending location.
     * @param particle The particle to spawn.
     * @param spacing  The distance between each point.
     * @return The number of particles spawned.
     */
    public static int line(
            @NotNull final Location from,
            @NotNull final Location to,
            @NotNull final Particle particle,
            final double spacing
    ) {
        return line(from, to, particle, spacing, null);
    }

    /**
     * Spawns a line between two locations.
     *
     * @param from     The starting location.
     * @param to       The ending location.
     * @param particle The particle to spawn.
     * @param spacing  The distance between each point.
     * @param data     Optional particle data.
     * @param <T>      Particle data type.
     * @return The number of particles spawned.
     */
    public static <T> int line(
            @NotNull final Location from,
            @NotNull final Location to,
            @NotNull final Particle particle,
            final double spacing,
            @Nullable final T data
    ) {
        validatePositive(spacing, "spacing");

        final World world = requireWorld(from);
        final World targetWorld = requireWorld(to);

        if(!Objects.equals(world, targetWorld)) {
            throw new IllegalArgumentException("Both locations must be in the same world");
        }

        final double distance = from.distance(to);
        if(distance == 0D) {
            spawn(world, from, particle, data);
            return 1;
        }

        final int steps = Math.max(1, (int) Math.ceil(distance / spacing));
        final int points = steps + 1;

        for(int i = 0; i < points; i++) {
            final double progress = i / (double) steps;
            final Location point = from.clone().add(to.clone().subtract(from).multiply(progress));
            spawn(world, point, particle, data);
        }

        return points;
    }

    /**
     * Spawns a helix around a center point.
     *
     * @param center   The helix center.
     * @param radius   The helix radius.
     * @param height   The helix height. Can be negative to reverse the direction.
     * @param particle The particle to spawn.
     * @return The number of particles spawned.
     */
    public static int helix(
            @NotNull final Location center,
            final double radius,
            final double height,
            @NotNull final Particle particle
    ) {
        return helix(center, radius, height, particle, DEFAULT_HELIX_TURNS, DEFAULT_HELIX_POINTS_PER_TURN, null);
    }

    /**
     * Spawns a helix around a center point.
     *
     * @param center        The helix center.
     * @param radius        The helix radius.
     * @param height        The helix height. Can be negative to reverse the direction.
     * @param particle      The particle to spawn.
     * @param turns         The number of turns.
     * @param pointsPerTurn The number of points per turn.
     * @return The number of particles spawned.
     */
    public static int helix(
            @NotNull final Location center,
            final double radius,
            final double height,
            @NotNull final Particle particle,
            final int turns,
            final int pointsPerTurn
    ) {
        return helix(center, radius, height, particle, turns, pointsPerTurn, null);
    }

    /**
     * Spawns a helix around a center point.
     *
     * @param center        The helix center.
     * @param radius        The helix radius.
     * @param height        The helix height. Can be negative to reverse the direction.
     * @param particle      The particle to spawn.
     * @param turns         The number of turns.
     * @param pointsPerTurn The number of points per turn.
     * @param data          Optional particle data.
     * @param <T>           Particle data type.
     * @return The number of particles spawned.
     */
    public static <T> int helix(
            @NotNull final Location center,
            final double radius,
            final double height,
            @NotNull final Particle particle,
            final int turns,
            final int pointsPerTurn,
            @Nullable final T data
    ) {
        validateNonNegative(radius, "radius");
        validatePositive(turns, "turns");
        validatePositive(pointsPerTurn, "pointsPerTurn");

        final World world = requireWorld(center);
        final int totalPoints = turns * pointsPerTurn;
        final double stepHeight = height / totalPoints;
        int spawned = 0;

        for(int i = 0; i <= totalPoints; i++) {
            final double progress = i / (double) totalPoints;
            final double angle = FULL_ROTATION * turns * progress;
            final Location point = center.clone().add(
                    radius * Math.cos(angle),
                    stepHeight * i,
                    radius * Math.sin(angle)
            );

            spawn(world, point, particle, data);
            spawned++;
        }

        return spawned;
    }

    /**
     * Spawns a burst of particles in a random sphere around a center point.
     *
     * @param center   The burst center.
     * @param particle The particle to spawn.
     * @param count    The number of particles to spawn.
     * @param radius   The maximum spread radius.
     * @return The number of particles spawned.
     */
    public static int burst(
            @NotNull final Location center,
            @NotNull final Particle particle,
            final int count,
            final double radius
    ) {
        return burst(center, particle, count, radius, null);
    }

    /**
     * Spawns a burst of particles in a random sphere around a center point.
     *
     * @param center   The burst center.
     * @param particle The particle to spawn.
     * @param count    The number of particles to spawn.
     * @param radius   The maximum spread radius.
     * @param data     Optional particle data.
     * @param <T>      Particle data type.
     * @return The number of particles spawned.
     */
    public static <T> int burst(
            @NotNull final Location center,
            @NotNull final Particle particle,
            final int count,
            final double radius,
            @Nullable final T data
    ) {
        validatePositive(count, "count");
        validateNonNegative(radius, "radius");

        final World world = requireWorld(center);
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        int spawned = 0;

        for(int i = 0; i < count; i++) {
            final double theta = random.nextDouble(FULL_ROTATION);
            final double phi = Math.acos(2D * random.nextDouble() - 1D);
            final double distance = radius * Math.cbrt(random.nextDouble());
            final double sinPhi = Math.sin(phi);
            final Location point = center.clone().add(
                    distance * sinPhi * Math.cos(theta),
                    distance * Math.cos(phi),
                    distance * sinPhi * Math.sin(theta)
            );

            spawn(world, point, particle, data);
            spawned++;
        }

        return spawned;
    }

    private static void spawn(
            @NotNull final World world,
            @NotNull final Location location,
            @NotNull final Particle particle,
            @Nullable final Object data
    ) {
        if(data == null) {
            world.spawnParticle(particle, location, 1, 0D, 0D, 0D, 0D);
            return;
        }

        world.spawnParticle(particle, location, 1, 0D, 0D, 0D, 0D, data);
    }

    private static @NotNull World requireWorld(@NotNull final Location location) {
        return Objects.requireNonNull(location.getWorld(), "Location world cannot be null");
    }

    private static void validatePositive(final double value, final String name) {
        if(value <= 0D) {
            throw new IllegalArgumentException(name + " must be greater than 0");
        }
    }

    private static void validatePositive(final int value, final String name) {
        if(value <= 0) {
            throw new IllegalArgumentException(name + " must be greater than 0");
        }
    }

    private static void validateNonNegative(final double value, final String name) {
        if(value < 0D) {
            throw new IllegalArgumentException(name + " must be greater than or equal to 0");
        }
    }
}


