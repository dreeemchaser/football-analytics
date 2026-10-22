package football.analytics.draai.co.engine;

import football.analytics.draai.co.model.TeamRecord;

import java.util.Comparator;

public class StandingsOrder {

    private StandingsOrder() {}

    /** 1974/75 order: points desc, goal average desc, goals scored desc, name asc. */
    public static final Comparator<TeamRecord> COMPARATOR =
            Comparator.<TeamRecord, Integer>comparing(TeamRecord::points).reversed()
                    .thenComparing(Comparator.<TeamRecord, Double>comparing(TeamRecord::goalAverage).reversed())
                    .thenComparing(Comparator.<TeamRecord, Integer>comparing(TeamRecord::goalsFor).reversed())
                    .thenComparing(TeamRecord::team);
}
