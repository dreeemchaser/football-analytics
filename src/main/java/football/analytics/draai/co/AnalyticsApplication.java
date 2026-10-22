package football.analytics.draai.co;

import football.analytics.draai.co.engine.StandingsCalculator;
import football.analytics.draai.co.io.LeagueTableFormatter;
import football.analytics.draai.co.io.MatchReader;
import football.analytics.draai.co.model.Match;
import football.analytics.draai.co.model.TeamRecord;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public class AnalyticsApplication {

	public static void main(String[] args) {
		// resolve input: file arg or stdin
		InputStream input = System.in;
		if (args.length >= 1) {
			try {
				input = new FileInputStream(args[0]);
			} catch (IOException e) {
				System.err.println("ERROR: cannot open input file: " + args[0]);
				System.exit(1);
			}
		}

		// resolve output: file arg or stdout
		PrintStream output = System.out;
		if (args.length >= 2) {
			try {
				output = new PrintStream(new FileOutputStream(args[1]));
			} catch (IOException e) {
				System.err.println("ERROR: cannot open output file: " + args[1]);
				System.exit(1);
			}
		}

		try {
			// read → calculate → format → write
			List<Match> matches       = new MatchReader().read(input);
			List<TeamRecord> standings = new StandingsCalculator().calculate(matches);
			String table              = new LeagueTableFormatter().format(standings);
			output.print(table);
		} catch (IOException e) {
			System.err.println("ERROR: " + e.getMessage());
			System.exit(1);
		} finally {
			if (output != System.out) output.close();
			try { if (input != System.in) input.close(); } catch (IOException ignored) {}
		}
	}
}
