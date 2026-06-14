package dev.fooble.mc.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Getter
@Accessors(fluent = true)
public final class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    private ItemBuilder(@NotNull final ItemStack base) {
        this.item = base.clone();
        this.meta = Objects.requireNonNull(
                this.item.getItemMeta(),
                "ItemMeta cannot be null"
        );
    }

    /**
     * Creates a new ItemBuilder with the given material as the base item.
     *
     * @param material The material to use as the base item.
     * @return A new ItemBuilder instance.
     */
    public static @NotNull ItemBuilder of(@NotNull final Material material) {
        return new ItemBuilder(new ItemStack(material));
    }

    /**
     * Sets the display name of the item.
     *
     * @param name The display name to set.
     * @return The current ItemBuilder instance.
     */
    public @NotNull ItemBuilder name(@Nullable final Component name) {
        meta.displayName(name);
        return this;
    }

    /**
     * Sets the lore of the item. (Replaces any existing lore)
     *
     * @param lore The lore to set.
     * @return The current ItemBuilder instance.
     */
    public @NotNull ItemBuilder lore(@NotNull final List<Component> lore) {
        meta.lore(lore);
        return this;
    }

    /**
     * Sets the lore of the item. (Replaces any existing lore)
     *
     * @param lines The lore lines to set.
     * @return The current ItemBuilder instance.
     */
    public @NotNull ItemBuilder lore(@NotNull final Component... lines) {
        return lore(List.of(lines));
    }

    /**
     * Adds lines to the existing lore of the item.
     *
     * @param lines The lore lines to add.
     * @return The current ItemBuilder instance.
     */
    public @NotNull ItemBuilder addLore(@NotNull final Component... lines) {
        final List<Component> lore = new ArrayList<>(
                Optional.ofNullable(meta.lore()).orElse(List.of())
        );
        lore.addAll(List.of(lines));
        meta.lore(lore);
        return this;
    }

    /**
     * Sets the amount of the item.
     *
     * @param amount The amount to set.
     * @return The current ItemBuilder instance.
     */
    public @NotNull ItemBuilder amount(final int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * Sets whether the item is unbreakable.
     *
     * @param unbreakable Whether the item should be unbreakable.
     * @return The current ItemBuilder instance.
     */
    public @NotNull ItemBuilder unbreakable(final boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Adds item flags to the item.
     *
     * @param flags The item flags to add.
     * @return The current ItemBuilder instance.
     */
    public @NotNull ItemBuilder flags(@NotNull final ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    /**
     * Removes item flags from the item.
     *
     * @param flags The item flags to remove.
     * @return The current ItemBuilder instance.
     */
    public @NotNull ItemBuilder removeFlags(@NotNull final ItemFlag... flags) {
        meta.removeItemFlags(flags);
        return this;
    }

    /**
     * Adds an enchantment to the item.
     *
     * @param enchantment The enchantment to add.
     * @param level       The level of the enchantment.
     * @return The current ItemBuilder instance.
     */
    public @NotNull ItemBuilder enchant(
            @NotNull final Enchantment enchantment,
            final int level
    ) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Removes an enchantment from the item.
     *
     * @param enchantment The enchantment to remove.
     * @return The current ItemBuilder instance.
     */
    public @NotNull ItemBuilder removeEnchantment(
            @NotNull final Enchantment enchantment
    ) {
        meta.removeEnchant(enchantment);
        return this;
    }

    /**
     * Adds a glowing effect to the item.
     *
     * @return The current ItemBuilder instance.
     */
    public @NotNull ItemBuilder glow() {
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * Sets the owner of a skull item.
     *
     * @param player The owner of the skull.
     * @return The current ItemBuilder instance.
     */
    public @NotNull ItemBuilder skullOwner(
            @NotNull final OfflinePlayer player
    ) {
        if(meta instanceof final SkullMeta skullMeta) {
            skullMeta.setOwningPlayer(player);
        }
        return this;
    }

    /**
     * Sets the owner of a skull item using their name.
     *
     * @param name The name of the owner of the skull.
     * @return The current ItemBuilder instance.
     */
    public @NotNull ItemBuilder skullOwner(@NotNull final String name) {
        if(meta instanceof final SkullMeta skullMeta) {
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        }
        return this;
    }

    public @NotNull ItemBuilder skullTexture(@NotNull final String textureUrl) {
        if(!(meta instanceof final SkullMeta skullMeta)) return this;

        final String json = """
                {
                  "textures": {
                    "SKIN": {
                      "url": "%s"
                    }
                  }
                }
                """.formatted(textureUrl);

        final String encoded = Base64.getEncoder().encodeToString(
                json.getBytes(StandardCharsets.UTF_8)
        );

        final PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty("textures", encoded));

        skullMeta.setPlayerProfile(profile);
        return this;
    }

    /**
     * Sets the color of leather armor.
     *
     * @param color The color of the leather armor.
     * @return The current ItemBuilder instance.
     */
    public @NotNull ItemBuilder leatherColor(@NotNull final Color color) {
        if(meta instanceof final LeatherArmorMeta leatherMeta) {
            leatherMeta.setColor(color);
        }
        return this;
    }

    /**
     * Sets a PersistentDataContainer value.
     *
     * @param key   The namespaced key.
     * @param type  The type of the container.
     * @param value The value of the container.
     * @return The current ItemBuilder instance.
     */
    public <T, Z> @NotNull ItemBuilder data(
            @NotNull final NamespacedKey key,
            @NotNull final PersistentDataType<T, Z> type,
            @NotNull final Z value
    ) {
        meta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    /**
     * Removes a PersistentDataContainer value.
     *
     * @param key The namespaced key of the container.
     * @return The current ItemBuilder instance.
     */
    public @NotNull ItemBuilder removeData(@NotNull final NamespacedKey key) {
        meta.getPersistentDataContainer().remove(key);
        return this;
    }

    /**
     * Builds the ItemStack.
     *
     * @return The built item.
     */
    public @NotNull ItemStack build() {
        item.setItemMeta(meta);
        return item.clone();
    }
}
