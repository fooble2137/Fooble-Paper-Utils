package dev.fooble.mc.dialogue;

import io.papermc.paper.dialog.DialogResponseView;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface DialogueAction {

    void run(@NotNull Player player, @NotNull DialogResponseView view);

}