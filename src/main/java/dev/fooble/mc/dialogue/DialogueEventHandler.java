package dev.fooble.mc.dialogue;

import io.papermc.paper.connection.PlayerGameConnection;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class DialogueEventHandler implements Listener {

    private final HashMap<String, DialogueAction> actions = new HashMap<>();

    public DialogueEventHandler(@NotNull JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void register(String string, DialogueAction action) {
        actions.put(string, action);
    }

    @EventHandler
    public void handlePlayerDialogueClick(@NotNull PlayerCustomClickEvent event) {
        final DialogueAction action = actions.get(event.getIdentifier().asString());
        if(action == null) return;

        final DialogResponseView view = event.getDialogResponseView();
        if(view == null) return;

        if(!(event.getCommonConnection() instanceof PlayerGameConnection connection)) return;

        final Player player = connection.getPlayer();
        action.run(player, view);
    }

}