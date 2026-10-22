package football.analytics.draai.co.model;

// Remember records are immutable
public record Match(
    String homeTeam, 
    String awayTeam, 
    int homeGoals, 
    int awayGoals
){}
