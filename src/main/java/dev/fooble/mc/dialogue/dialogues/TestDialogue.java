package dev.fooble.mc.dialogue.dialogues;

import dev.fooble.mc.components.ComponentBuilder;
import dev.fooble.mc.dialogue.DialogueContent;
import dev.fooble.mc.dialogue.DialogueEventHandler;
import dev.fooble.mc.dialogue.DialogueHeader;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TestDialogue implements DialogueContent {

    @Override
    public @NotNull Component getTitle() {
        return ComponentBuilder.of("Configure your new experience value");
    }

    @Override
    public @Nullable DialogueHeader getHeader() {
        return null;
    }

    @Override
    public @NotNull List<DialogInput> getInputs() {
        return List.of(
                DialogInput.numberRange("level", ComponentBuilder.of("<green>Level"), 0f, 100f)
                        .step(1f)
                        .initial(0f)
                        .width(300)
                        .build(),
                DialogInput.numberRange("experience", ComponentBuilder.of("<green>Experience"), 0f, 100f)
                        .step(1f)
                        .initial(0f)
                        .labelFormat("%s: %s percent to the next level")
                        .width(300)
                        .build()
        );
    }

    @Override
    public @NotNull DialogType getDialogType() {
        return DialogType.confirmation(
                ActionButton.create(
                        ComponentBuilder.of("<dark_green>Confirm"),
                        ComponentBuilder.of("Click to confirm your input."),
                        100,
                        DialogAction.customClick(Key.key("fooble:dialog/test/confirm"), null)
                ),
                ActionButton.create(
                        ComponentBuilder.of("<dark_red>Discard"),
                        ComponentBuilder.of("Click to discard your input."),
                        100,
                        null
                )
        );
    }

    @Override
    public void registerActions(@NotNull DialogueEventHandler eventHandler) {
        eventHandler.register("fooble:dialog/test/confirm", (player, view) -> {
            int levels = view.getFloat("level").intValue();
            float exp = view.getFloat("experience").floatValue();

            player.sendMessage(ComponentBuilder.of("You selected " + levels + " levels and " + exp + "% exp to the next level!"));
            player.setLevel(levels);
            player.setExp(exp / 100);
        });
    }

    @Override
    public boolean canCloseWithEscape() {
        return true;
    }
}
