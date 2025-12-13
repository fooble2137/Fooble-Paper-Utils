package dev.fooble.mc.components;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface Snippet {

    @NotNull
    Component toComponent();
}
