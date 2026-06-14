package dev.fooble.mc.dialogue;

import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DialogueContent {

    @NotNull
    Component getTitle();

    @Nullable
    DialogueHeader getHeader();

    @NotNull
    List<DialogInput> getInputs();

    @NotNull
    DialogType getDialogType();

    boolean canCloseWithEscape();

    @NotNull
    default DialogBase.DialogAfterAction getAfterAction() {
        return DialogBase.DialogAfterAction.CLOSE;
    }

    void registerActions(@NotNull DialogueEventHandler eventHandler);
}