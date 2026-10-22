package football.analytics.draai.co.model;

public class TeamRecord {

    String team;
    int played, won, drawn, lost, goalsFor, goalsAgainst;

    public TeamRecord(String team) {
        this.team = team;
    }

    public TeamRecord(String team, int won, int drawn, int lost, int goalsFor, int goalsAgainst) {
        this.team = team;
        this.won = won;
        this.drawn = drawn;
        this.lost = lost;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
    }

    // Old league rules: (W) = 2, (D) = 1.
    public int points() {
        return (2 * won) + drawn;
    }

    public double goalAverage(){
        return goalsAgainst == 0
                ? Double.MAX_VALUE
                : (double) goalsFor / goalsAgainst;
    }

    public void apply(Match m, boolean isHome){
        int teamGoals = isHome ? m.homeGoals() : m.awayGoals();
        int opponentGoals = isHome ? m.awayGoals() : m.homeGoals();

        goalsFor += teamGoals;
        goalsAgainst += opponentGoals;

        if (teamGoals > opponentGoals) won++;
        else if (teamGoals < opponentGoals) lost++;
        else drawn++;
    }

    public String team()      { return team; }
    public int played()       { return won + drawn + lost; }   // derived, can't drift
    public int won()          { return won; }
    public int drawn()        { return drawn; }
    public int lost()         { return lost; }
    public int goalsFor()     { return goalsFor; }
    public int goalsAgainst() { return goalsAgainst; }
}
