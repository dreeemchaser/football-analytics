package football.analytics.draai.co.engine;

import football.analytics.draai.co.model.Match;
import football.analytics.draai.co.model.TeamRecord;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StandingsCalculator {

    /**
     * Builds a sorted league standings table from a list of match results.
     *
     * For each match both the home and away TeamRecord are updated via apply().
     * The resulting list is sorted using the 1974/75 rules defined in StandingsOrder:
     *   1. Points (desc)
     *   2. Goal average (desc)
     *   3. Goals for (desc)
     *   4. Team name (asc) — for determinism
     */
    public List<TeamRecord> calculate(List<Match> matches) {
        Map<String, TeamRecord> table = new LinkedHashMap<>();

        for (Match match : matches) {
            TeamRecord home = table.computeIfAbsent(match.homeTeam(), TeamRecord::new);
            TeamRecord away = table.computeIfAbsent(match.awayTeam(), TeamRecord::new);

            home.apply(match, true);
            away.apply(match, false);
        }

        List<TeamRecord> standings = new ArrayList<>(table.values());
        standings.sort(StandingsOrder.COMPARATOR);
        return standings;
    }
}
