# Project steering

## Purpose and technology

This is a Java 21 command-line application built with Gradle. It reads football match results and calculates a league table. Keep the implementation dependency-light and platform-agnostic.

## Historical rules

- A win earns 2 points, a draw earns 1, and a loss earns 0.
- Sort by points descending, goal average (goals for divided by goals against) descending, goals scored descending, then team name ascending for deterministic output.
- Goal average is not goal difference.

## Input and output

- Input records use `HomeTeam,AwayTeam,HomeGoals,AwayGoals`, one match per line.
- Blank lines and lines beginning with `#` are ignored. Malformed records are warned about on stderr and skipped.
- Output is CSV with columns `Pos,Team,P,W,D,L,F,A,GA,Pts`.
- The application accepts an optional input filename and optional output filename; without them it uses stdin and stdout.

## Code organization

- Keep domain objects in `model/`, standings calculation and ordering in `engine/`, and parsing/formatting in `io/`.
- Keep `AnalyticsApplication` focused on command-line and file I/O orchestration.
- Do not add a framework dependency for the CLI.
