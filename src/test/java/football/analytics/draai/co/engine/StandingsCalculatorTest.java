package football.analytics.draai.co.engine;

import football.analytics.draai.co.model.Match;
import football.analytics.draai.co.model.TeamRecord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StandingsCalculatorTest {

    private final StandingsCalculator calc = new StandingsCalculator();

    // ── basic ordering ────────────────────────────────────────────────────────

    @Test
    void team_with_most_points_is_first() {
        List<Match> matches = List.of(
                new Match("Alpha", "Beta", 2, 0),   // Alpha wins
                new Match("Beta",  "Gamma", 0, 0),  // draw
                new Match("Alpha", "Gamma", 1, 0)   // Alpha wins
        );
        List<TeamRecord> table = calc.calculate(matches);
        assertEquals("Alpha", table.get(0).team());
    }

    @Test
    void returns_one_record_per_team() {
        List<Match> matches = List.of(
                new Match("Alpha", "Beta",  1, 0),
                new Match("Beta",  "Gamma", 1, 1),
                new Match("Gamma", "Alpha", 0, 2)
        );
        assertEquals(3, calc.calculate(matches).size());
    }

    @Test
    void empty_match_list_returns_empty_table() {
        assertTrue(calc.calculate(List.of()).isEmpty());
    }

    // ── THE KEY TEST: goal average tiebreaker (not goal difference) ───────────
    //
    // Team A: 1W  — scored 2, conceded 1  → pts=2, GA=2.000, GD=+1
    // Team B: 1W  — scored 4, conceded 3  → pts=2, GA=1.333, GD=+1
    //
    // Goal difference is equal (+1 each). Goal average is NOT — A wins on GA.
    // If the code used GD instead of GA, these would be equal and fall to name order.

    @Test
    void tiebreaker_uses_goal_average_not_goal_difference() {
        List<Match> matches = List.of(
                new Match("TeamA", "Dummy1", 2, 1),  // A: 2F 1A → GA 2.000
                new Match("TeamB", "Dummy2", 4, 3),  // B: 4F 3A → GA 1.333
                new Match("Dummy1", "Dummy2", 0, 0)  // keep Dummy teams active
        );
        List<TeamRecord> table = calc.calculate(matches);

        TeamRecord first  = table.get(0);
        TeamRecord second = table.get(1);

        assertEquals("TeamA", first.team(),
                "TeamA has better goal average (2.000 vs 1.333) and should rank above TeamB " +
                "even though both have equal goal difference (+1)");
        assertEquals("TeamB", second.team());
    }

    // ── points accumulate correctly ───────────────────────────────────────────

    @Test
    void win_gives_two_points() {
        List<TeamRecord> table = calc.calculate(List.of(
                new Match("Winner", "Loser", 1, 0)
        ));
        TeamRecord winner = table.stream().filter(r -> r.team().equals("Winner")).findFirst().orElseThrow();
        assertEquals(2, winner.points());
    }

    @Test
    void draw_gives_one_point_each() {
        List<TeamRecord> table = calc.calculate(List.of(
                new Match("Home", "Away", 1, 1)
        ));
        table.forEach(r -> assertEquals(1, r.points(), r.team() + " should have 1 point"));
    }

    @Test
    void goals_are_attributed_to_correct_teams() {
        List<TeamRecord> table = calc.calculate(List.of(
                new Match("Home", "Away", 3, 1)
        ));
        TeamRecord home = table.stream().filter(r -> r.team().equals("Home")).findFirst().orElseThrow();
        TeamRecord away = table.stream().filter(r -> r.team().equals("Away")).findFirst().orElseThrow();

        assertEquals(3, home.goalsFor());
        assertEquals(1, home.goalsAgainst());
        assertEquals(1, away.goalsFor());
        assertEquals(3, away.goalsAgainst());
    }

    // ── alphabetical fallback ─────────────────────────────────────────────────

    @Test
    void teams_identical_on_all_criteria_are_sorted_alphabetically() {
        // Both teams draw 1-1 with each other — identical pts, GA, goals for
        List<Match> matches = List.of(
                new Match("Zebra", "Aardvark", 1, 1)
        );
        List<TeamRecord> table = calc.calculate(matches);
        // Both have 1pt, GA 1.000, 1 GF — alphabetical is the only differentiator
        assertEquals("Aardvark", table.get(0).team());
        assertEquals("Zebra",    table.get(1).team());
    }
}
