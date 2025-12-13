package dev.fooble.mc.dialogue;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Dialogue {

    @NotNull
    private final DialogueContent content;

    @NotNull
    private final JavaPlugin plugin;

    public Dialogue(@NotNull DialogueContent content, @NotNull JavaPlugin plugin) {
        this.content = content;
        this.plugin = plugin;
    }

    public void open(Player player) {
        final List<DialogBody> bodies = new ArrayList<>();

        final DialogueHeader header = content.getHeader();

        if(header != null) {
            if(header.text() != null) {
                bodies.add(DialogBody.plainMessage(header.text()));
            }
            if(header.item() != null) {
                bodies.add(DialogBody.item(header.item()).build());
            }
        }

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(content.getTitle())
                        .body(bodies)
                        .inputs(content.getInputs())
                        .build()
                )
                .type(content.getDialogType()));

        content.registerActions(DialogueUtils.getEventHandler(plugin));
        player.showDialog(dialog);
    }

}