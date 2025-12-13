package dev.fooble.mc.components;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public final class ComponentBuilder {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @NotNull
    public static Component of(Object @NotNull ... parts) {
        Component result = Component.empty();

        for(final Object part : parts) {
            result = switch(part) {
                case Component component -> result.append(component);
                case String string -> result.append(MINI_MESSAGE.deserialize(string));
                case Snippet snippet -> result.append(snippet.toComponent());
                default -> result.append(MINI_MESSAGE.deserialize(String.valueOf(part)));
            };
        }

        return result;
    }
}