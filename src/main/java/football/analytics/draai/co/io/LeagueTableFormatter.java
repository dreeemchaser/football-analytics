package football.analytics.draai.co.io;

import football.analytics.draai.co.model.TeamRecord;

import java.util.List;
import java.util.Locale;

public class LeagueTableFormatter {

    /**
     * Formats a pre-sorted list of TeamRecords as CSV.
     *
     * Column order: Pos, Team, P, W, D, L, F, A, GA, Pts.
     * Goal average is displayed to 3 decimal places, or as ∞ if goals against is zero.
     */
    public String format(List<TeamRecord> standings) {
        StringBuilder csv = new StringBuilder("Pos,Team,P,W,D,L,F,A,GA,Pts")
                .append(System.lineSeparator());
        for (int i = 0; i < standings.size(); i++) {
            TeamRecord r = standings.get(i);

            String goalAverage = r.goalsAgainst() == 0
                    ? "∞"
                    : String.format(Locale.ROOT, "%.3f", r.goalAverage());

            csv.append(i + 1).append(',')
                    .append(escapeCsv(r.team())).append(',')
                    .append(r.played()).append(',')
                    .append(r.won()).append(',')
                    .append(r.drawn()).append(',')
                    .append(r.lost()).append(',')
                    .append(r.goalsFor()).append(',')
                    .append(r.goalsAgainst()).append(',')
                    .append(goalAverage).append(',')
                    .append(r.points())
                    .append(System.lineSeparator());
        }
        return csv.toString();
    }

    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
