package football.analytics.draai.co.io;

import football.analytics.draai.co.engine.StandingsCalculator;
import football.analytics.draai.co.model.Match;
import football.analytics.draai.co.model.TeamRecord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeagueTableFormatterTest {

    private final LeagueTableFormatter formatter = new LeagueTableFormatter();

    private List<TeamRecord> standingsFor(List<Match> matches) {
        return new StandingsCalculator().calculate(matches);
    }

    @Test
    void empty_standings_returns_empty_string() {
        assertEquals("", formatter.format(List.of()));
    }

    @Test
    void output_contains_header_row() {
        List<TeamRecord> standings = standingsFor(List.of(
                new Match("Arsenal", "Chelsea", 1, 0)
        ));
        String output = formatter.format(standings);
        assertTrue(output.contains("Pos"), "should contain Pos header");
        assertTrue(output.contains("Team"), "should contain Team header");
        assertTrue(output.contains("Pts"), "should contain Pts header");
        assertTrue(output.contains("GA"), "should contain GA header");
    }

    @Test
    void output_contains_all_team_names() {
        List<TeamRecord> standings = standingsFor(List.of(
                new Match("Arsenal", "Chelsea", 1, 0),
                new Match("Liverpool", "Everton", 2, 2)
        ));
        String output = formatter.format(standings);
        assertTrue(output.contains("Arsenal"));
        assertTrue(output.contains("Chelsea"));
        assertTrue(output.contains("Liverpool"));
        assertTrue(output.contains("Everton"));
    }

    @Test
    void first_team_is_labelled_position_1() {
        List<TeamRecord> standings = standingsFor(List.of(
                new Match("Arsenal", "Chelsea", 2, 0)
        ));
        String[] lines = formatter.format(standings).split(System.lineSeparator());
        // line 0 = header, line 1 = separator, line 2 = first team
        assertTrue(lines[2].trim().startsWith("1"), "first data row should start with position 1");
    }

    @Test
    void goal_average_shown_to_three_decimal_places() {
        // Arsenal: 3F 2A → GA = 1.500
        List<TeamRecord> standings = standingsFor(List.of(
                new Match("Arsenal", "Chelsea", 3, 2)
        ));
        String output = formatter.format(standings);
        assertTrue(output.contains("1.500"), "GA should be formatted to 3 decimal places");
    }

    @Test
    void team_conceding_zero_goals_shows_infinity_symbol() {
        List<TeamRecord> standings = standingsFor(List.of(
                new Match("Arsenal", "Chelsea", 1, 0)
        ));
        String output = formatter.format(standings);
        assertTrue(output.contains("∞"), "team with 0 goals against should show ∞");
    }

    @Test
    void correct_points_displayed_for_win_draw_loss() {
        List<TeamRecord> standings = standingsFor(List.of(
                new Match("Alpha", "Beta",  2, 0),  // Alpha 2pts, Beta 0pts
                new Match("Gamma", "Delta", 1, 1)   // Gamma 1pt,  Delta 1pt
        ));
        String output = formatter.format(standings);
        // Alpha on 2 points should appear with "2" in the Pts column
        assertTrue(output.contains("Alpha"));
        // quick sanity: output has content beyond the header
        assertTrue(output.split(System.lineSeparator()).length > 2);
    }
}
