# Football League Table Calculator

A production-ready command-line application that calculates football league standings from match results. Built to compute the English First Division table after the 10th week of the 1974/75 season.

## Requirements

- Java 21+ (Gradle will auto-provision via the foojay toolchain resolver if not installed)
- Gradle wrapper included — no separate Gradle installation needed

## Build

```bash
./gradlew build
```

## Run

### With a file argument (recommended)

```bash
./gradlew run --args="data/data.csv"
```

### With input and output files

```bash
./gradlew run --args="data/data.csv data/standings.csv"
```

### Via stdin / stdout

```bash
cat data/data.csv | ./gradlew -q run --console=plain
```

### As a standalone JAR

```bash
./gradlew jar
java -cp build/libs/analytics-0.0.1-SNAPSHOT.jar football.analytics.draai.co.AnalyticsApplication data/data.csv
```

## Input format

CSV file, one match per line:

```
HomeTeam,AwayTeam,HomeGoals,AwayGoals
```

- Lines starting with `#` are treated as comments and skipped
- Blank lines are skipped
- Malformed lines produce a warning on stderr and are skipped — processing continues

Example:

```
# Week 1 - 1974-08-17
Stoke City,Leeds United,3,0
Everton,Derby County,0,0
Manchester City,West Ham United,4,0
```

## Output format

Comma-separated values are printed to stdout (or written to a file if a second argument is provided):

```csv
Pos,Team,P,W,D,L,F,A,GA,Pts
1,Ipswich Town,10,8,0,2,18,6,3.000,16
2,Manchester City,10,6,2,2,14,11,1.273,14
...
```

Columns: Position, Team, Played, Won, Drawn, Lost, Goals For, Goals Against, Goal Average, Points. Team names containing commas, quotes, or line breaks are escaped according to CSV conventions. Goal Average is shown to three decimal places, or as `∞` if a team has conceded no goals.

## League rules (1974/75 English First Division)

- Win = 2 points, Draw = 1 point, Loss = 0 points
- Tiebreaker 1: Goal Average (Goals For ÷ Goals Against) — **not** goal difference
- Tiebreaker 2: Goals scored
- Tiebreaker 3: Team name (alphabetical) for determinism
- The switch from goal average to goal difference did not happen until the 1976/77 season

## Run tests

```bash
./gradlew test
```

Test report: `build/reports/tests/test/index.html`

## Data files

| File | Description |
|---|---|
| `data/data.csv` | Match results for the English First Division, weeks 1–10 of the 1974/75 season (107 matches, sourced from FBref) |
| `data/standings.csv` | CSV standings output produced by running the application against the above input |

## Sources

- Match results: [FBref 1974–75 First Division scores and fixtures](https://fbref.com/en/comps/9/1974-1975/schedule/1974-1975-Premier-League-Scores-and-Fixtures)
- Historical points and tiebreak rules: [11v11 league table notes](https://www.11v11.com/league-tables/league-division-one/1976/), which document two points for a win through 1980/81 and goal average as the English league tiebreak method through 1995/96.

## Project structure

```
src/main/java/football/analytics/draai/co/
├── AnalyticsApplication.java      # Entry point — wires I/O, delegates to engine
├── model/
│   ├── Match.java                 # Immutable record: home/away teams and goals
│   └── TeamRecord.java            # Per-team accumulator: P W D L F A, points, GA
├── engine/
│   ├── StandingsCalculator.java   # List<Match> → sorted List<TeamRecord>
│   └── StandingsOrder.java        # Comparator implementing 1974/75 rules
└── io/
    ├── MatchReader.java           # CSV InputStream → List<Match>
    └── LeagueTableFormatter.java  # List<TeamRecord> → formatted String table
```
