package dev.fooble.mc.scoreboard;

import dev.fooble.mc.components.ComponentBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PlayerScoreboard {

    protected final Player player;
    protected final Scoreboard scoreboard;
    protected final Objective objective;

    private final Map<Integer, Team> lineTeams = new HashMap<>();
    private final List<String> entries = new ArrayList<>();

    protected PlayerScoreboard(Player player) {
        this.player = player;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        this.objective = scoreboard.registerNewObjective(
                "sidebar",
                Criteria.DUMMY,
                Component.empty()
        );
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        setupLines(getLines().size());
        update();
    }

    protected abstract String getTitle();

    protected abstract List<String> getLines();

    public void update() {
        objective.displayName(ComponentBuilder.of(getTitle()));

        final List<String> lines = getLines();
        int score = lines.size();

        for(int i = 0; i < lines.size(); i++) {
            final Team team = lineTeams.get(i);
            team.prefix(ComponentBuilder.of(lines.get(i)));
            objective.getScore(entries.get(i)).setScore(score--);
        }
    }

    public void show() {
        player.setScoreboard(scoreboard);
    }

    public void remove() {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    private void setupLines(int lineCount) {
        for(int i = 0; i < lineCount; i++) {
            final String entry = "§" + i;
            entries.add(entry);

            final Team team = scoreboard.registerNewTeam("line_" + i);
            team.addEntry(entry);

            lineTeams.put(i, team);
            objective.getScore(entry).setScore(lineCount - i);
        }
    }

}
