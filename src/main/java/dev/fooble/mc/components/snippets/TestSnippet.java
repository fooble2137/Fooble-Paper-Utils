package dev.fooble.mc.components.snippets;

import dev.fooble.mc.components.Snippet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class TestSnippet implements Snippet {

    @Override
    public @NotNull Component toComponent() {
        return MiniMessage.miniMessage().deserialize("haha!");
    }
}
