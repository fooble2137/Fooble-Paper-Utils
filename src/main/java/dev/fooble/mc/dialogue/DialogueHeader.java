package dev.fooble.mc.dialogue;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

public record DialogueHeader(Component text, ItemStack item) {

}