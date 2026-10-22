package football.analytics.draai.co.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamRecordTest {

    // ── points ────────────────────────────────────────────────────────────────

    @Test
    void points_win_scores_two() {
        TeamRecord r = new TeamRecord("Arsenal");
        r.apply(new Match("Arsenal", "Chelsea", 2, 0), true);
        assertEquals(2, r.points());
    }

    @Test
    void points_draw_scores_one() {
        TeamRecord r = new TeamRecord("Arsenal");
        r.apply(new Match("Arsenal", "Chelsea", 1, 1), true);
        assertEquals(1, r.points());
    }

    @Test
    void points_loss_scores_zero() {
        TeamRecord r = new TeamRecord("Arsenal");
        r.apply(new Match("Arsenal", "Chelsea", 0, 1), true);
        assertEquals(0, r.points());
    }

    // ── apply tracks W/D/L correctly ──────────────────────────────────────────

    @Test
    void apply_home_win_increments_correctly() {
        TeamRecord r = new TeamRecord("Home");
        r.apply(new Match("Home", "Away", 3, 1), true);
        assertEquals(1, r.played());
        assertEquals(1, r.won());
        assertEquals(0, r.drawn());
        assertEquals(0, r.lost());
        assertEquals(3, r.goalsFor());
        assertEquals(1, r.goalsAgainst());
    }

    @Test
    void apply_away_loss_increments_correctly() {
        TeamRecord r = new TeamRecord("Away");
        r.apply(new Match("Home", "Away", 3, 1), false);
        assertEquals(1, r.played());
        assertEquals(0, r.won());
        assertEquals(0, r.drawn());
        assertEquals(1, r.lost());
        assertEquals(1, r.goalsFor());
        assertEquals(3, r.goalsAgainst());
    }

    @Test
    void apply_accumulates_across_multiple_matches() {
        TeamRecord r = new TeamRecord("Team");
        r.apply(new Match("Team", "A", 2, 0), true);   // win
        r.apply(new Match("B", "Team", 1, 1), false);  // draw
        r.apply(new Match("Team", "C", 0, 3), true);   // loss
        assertEquals(3, r.played());
        assertEquals(1, r.won());
        assertEquals(1, r.drawn());
        assertEquals(1, r.lost());
        assertEquals(3, r.goalsFor());
        assertEquals(4, r.goalsAgainst());
        assertEquals(3, r.points());
    }

    // ── goalAverage ───────────────────────────────────────────────────────────

    @Test
    void goalAverage_is_goals_for_divided_by_goals_against() {
        TeamRecord r = new TeamRecord("Team");
        r.apply(new Match("Team", "A", 3, 2), true);
        assertEquals(1.5, r.goalAverage(), 0.001);
    }

    @Test
    void goalAverage_when_conceded_zero_returns_max_value() {
        TeamRecord r = new TeamRecord("Team");
        r.apply(new Match("Team", "A", 2, 0), true);
        assertEquals(Double.MAX_VALUE, r.goalAverage());
    }

    // ── played is derived ─────────────────────────────────────────────────────

    @Test
    void played_is_sum_of_w_d_l() {
        TeamRecord r = new TeamRecord("Team");
        r.apply(new Match("Team", "A", 1, 0), true);
        r.apply(new Match("B", "Team", 0, 0), false);
        assertEquals(2, r.played());
    }
}
