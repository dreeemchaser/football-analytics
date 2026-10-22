package football.analytics.draai.co.io;

import football.analytics.draai.co.model.Match;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchReaderTest {

    private List<Match> read(String csv) throws IOException {
        InputStream in = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8));
        return new MatchReader().read(in);
    }

    // ── happy path ────────────────────────────────────────────────────────────

    @Test
    void parses_single_valid_line() throws IOException {
        List<Match> matches = read("Arsenal,Chelsea,2,1");
        assertEquals(1, matches.size());
        assertEquals("Arsenal",  matches.get(0).homeTeam());
        assertEquals("Chelsea",  matches.get(0).awayTeam());
        assertEquals(2,          matches.get(0).homeGoals());
        assertEquals(1,          matches.get(0).awayGoals());
    }

    @Test
    void parses_multiple_lines() throws IOException {
        String csv = "Arsenal,Chelsea,2,1\nLiverpool,Everton,0,0\n";
        assertEquals(2, read(csv).size());
    }

    @Test
    void trims_whitespace_around_fields() throws IOException {
        List<Match> matches = read(" Arsenal , Chelsea , 3 , 0 ");
        assertEquals("Arsenal", matches.get(0).homeTeam());
        assertEquals("Chelsea", matches.get(0).awayTeam());
        assertEquals(3, matches.get(0).homeGoals());
    }

    @Test
    void skips_blank_lines() throws IOException {
        String csv = "\nArsenal,Chelsea,1,0\n\n";
        assertEquals(1, read(csv).size());
    }

    @Test
    void skips_comment_lines() throws IOException {
        String csv = "# this is a comment\nArsenal,Chelsea,1,0\n";
        assertEquals(1, read(csv).size());
    }

    @Test
    void returns_empty_list_for_empty_input() throws IOException {
        assertEquals(0, read("").size());
    }

    @Test
    void handles_zero_zero_draw() throws IOException {
        List<Match> matches = read("Derby County,Everton,0,0");
        assertEquals(0, matches.get(0).homeGoals());
        assertEquals(0, matches.get(0).awayGoals());
    }

    // ── malformed lines are skipped, not thrown ───────────────────────────────

    @Test
    void skips_line_with_wrong_field_count() throws IOException {
        String csv = "Arsenal,Chelsea,1\nLiverpool,Everton,0,0\n";
        assertEquals(1, read(csv).size());
    }

    @Test
    void skips_line_with_non_integer_goals() throws IOException {
        String csv = "Arsenal,Chelsea,two,1\nLiverpool,Everton,0,0\n";
        assertEquals(1, read(csv).size());
    }

    @Test
    void skips_line_with_negative_goals() throws IOException {
        String csv = "Arsenal,Chelsea,-1,0\nLiverpool,Everton,0,0\n";
        assertEquals(1, read(csv).size());
    }

    @Test
    void skips_line_with_blank_team_name() throws IOException {
        String csv = ",Chelsea,1,0\nLiverpool,Everton,0,0\n";
        assertEquals(1, read(csv).size());
    }

    @Test
    void valid_lines_after_bad_lines_are_still_parsed() throws IOException {
        String csv = "BAD_LINE\nArsenal,Chelsea,1,0\n";
        assertEquals(1, read(csv).size());
        assertEquals("Arsenal", read(csv).get(0).homeTeam());
    }
}
