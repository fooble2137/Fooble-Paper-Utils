package dev.fooble.mc.dialogue;

import org.bukkit.plugin.java.JavaPlugin;

public class DialogueUtils {

    private static DialogueEventHandler EVENT_HANDLER;

    public static DialogueEventHandler getEventHandler(JavaPlugin plugin) {
        if(EVENT_HANDLER == null) {
            EVENT_HANDLER = new DialogueEventHandler(plugin);
        }

        return EVENT_HANDLER;
    }
}