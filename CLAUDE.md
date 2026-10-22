# CLAUDE.md — Project instructions for AI assistants

This file provides context and instructions for AI coding assistants working on this project.

## What this project does

A command-line Java application that reads football match results from a CSV file and outputs a league standings table. It implements the exact rules used by the English First Division in the 1974/75 season.

## Build and test commands

```bash
# Build
./gradlew build

# Run all tests
./gradlew test

# Run the application with a file argument
./gradlew run --args="data/data.csv"

# Run with input and output files
./gradlew run --args="data/data.csv data/standings.csv"

# Compile only (fast check)
./gradlew compileJava
```

## Project structure

```
src/main/java/football/analytics/draai/co/
├── AnalyticsApplication.java   # main() — I/O wiring only
├── model/
│   ├── Match.java              # record(homeTeam, awayTeam, homeGoals, awayGoals)
│   └── TeamRecord.java         # mutable per-team stats accumulator
├── engine/
│   ├── StandingsCalculator.java
│   └── StandingsOrder.java     # Comparator — 1974/75 rules
└── io/
    ├── MatchReader.java        # CSV parser
    └── LeagueTableFormatter.java

src/test/java/football/analytics/draai/co/
├── model/TeamRecordTest.java
├── engine/StandingsCalculatorTest.java
└── io/
    ├── MatchReaderTest.java
    └── LeagueTableFormatterTest.java

data/
├── data.csv        # input — 107 First Division matches, weeks 1-10, 1974/75
└── standings.csv   # output — the resulting league table
```

## Key rules — do not change without understanding why

- **Points**: Win = 2, Draw = 1, Loss = 0. The 3-points-for-a-win rule did not exist in 1974/75.
- **Tiebreaker**: Goal Average (goals for ÷ goals against), NOT goal difference. The switch happened in 1976/77. Using goal difference is the most common mistake on this problem.
- **Sort order**: Points desc → Goal Average desc → Goals For desc → Team name asc
- This is encoded in `StandingsOrder.COMPARATOR`

## Conventions

- Java 21, plain Gradle (no Spring)
- No external runtime dependencies — only JUnit 5 for tests
- `MatchReader` accepts an `InputStream` so it works for both stdin and file input
- Malformed CSV lines are warned on stderr and skipped — never throw
- Team names come directly from the CSV — no normalisation is applied
