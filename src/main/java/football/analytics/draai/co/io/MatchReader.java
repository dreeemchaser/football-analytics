package football.analytics.draai.co.io;

import football.analytics.draai.co.model.Match;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MatchReader {

    /**
     * Reads match results from an InputStream.
     *
     * Expected CSV format (one match per line):
     *   HomeTeam,AwayTeam,HomeGoals,AwayGoals
     *
     * - Blank lines and lines starting with '#' are silently skipped.
     * - Malformed lines produce a warning on stderr and are skipped.
     * - Goals must be non-negative integers.
     */
    public List<Match> read(InputStream in) throws IOException {
        List<Match> matches = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, StandardCharsets.UTF_8))) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String trimmed = line.trim();

                // skip blanks and comment / header lines
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }

                String[] parts = trimmed.split(",", -1);

                // expected: man-city ,arsenal ,3 ,2
                if (parts.length != 4) {
                    warn(lineNumber, line, "expected 4 comma-separated fields, got " + parts.length);
                    continue;
                }

                String homeTeam = parts[0].trim();
                String awayTeam = parts[1].trim();
                String rawHome  = parts[2].trim();
                String rawAway  = parts[3].trim();

                if (homeTeam.isEmpty() || awayTeam.isEmpty()) {
                    warn(lineNumber, line, "team name must not be blank");
                    continue;
                }

                int homeGoals;
                int awayGoals;
                try {
                    homeGoals = Integer.parseInt(rawHome);
                    awayGoals = Integer.parseInt(rawAway);
                } catch (NumberFormatException e) {
                    warn(lineNumber, line, "goals must be integers");
                    continue;
                }

                if (homeGoals < 0 || awayGoals < 0) {
                    warn(lineNumber, line, "goals must be non-negative");
                    continue;
                }

                matches.add(new Match(homeTeam, awayTeam, homeGoals, awayGoals));
            }
        }

        return matches;
    }

    private void warn(int lineNumber, String line, String reason) {
        System.err.printf("WARNING: line %d skipped (%s): %s%n", lineNumber, reason, line);
    }
}
