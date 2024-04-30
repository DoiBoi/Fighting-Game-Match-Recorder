package main.model;

import org.json.JSONArray;
import org.json.JSONObject;
import main.persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a list of matches played
// Code regarding writing JSON file influenced by:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class Records implements Writable {

    private List<MatchInfo> records; // The list of matches played
    private EventLog log = EventLog.getInstance();

    // EFFECTS: Creates an empty list
    public Records() {
        records = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS:  adds the match information to the records
    public void addMatchInfo(MatchInfo match) {
        records.add(match);
        log.logEvent(new Event("Added a match"));
    }

    // EFFECTS: returns the records of matches played
    public List<MatchInfo> getMatchInfos() {
        log.logEvent(new Event("Retrieved the records"));
        return records;
    }

    // MODIFIES: this
    // EFFECTS:  removes the match info if it exists in the list of matches played
    public boolean removeMatchInfo(MatchInfo match) {
        if (records.contains(match)) {
            records.remove(match);
            return true;
        }
        return false;
    }

    // EFFECTS: returns the win-rate given the name of the character; it checks
    //          if the name of the character has been used for that match and adds win
    //          depending on the outcome.
    //          Additionally, this will not count matches where both players play the same
    //          characters
    public double getWinRate(String name) {
        int wins;
        int games;
        wins = 0;
        games = 0;

        if (records.isEmpty()) {
            return 0;
        } else {
            for (MatchInfo game : records) {
                if (game.getCharacter(1).equals(name) && !(game.getCharacter(2).equals(name))) {
                    wins += addWin(game.getOutcome());
                    games++;
                } else if (game.getCharacter(2).equals(name)
                        && !(game.getCharacter(1).equals(name))) {
                    boolean outcome = !game.getOutcome();
                    wins += addWin(outcome);
                    games++;
                }
            }
            if (games == 0) {
                return 0;
            }
            return round(wins, games);
        }
    }

    // EFFECTS: filters the matches based on if given character is used in the match
    public List<MatchInfo> filterMatches(String character) {
        List<MatchInfo> matchesWithChar = new ArrayList<>();
        for (MatchInfo match: records) {
            if (match.getCharacter(1).equals(character)
                    || match.getCharacter(2).equals(character)) {
                matchesWithChar.add(match);
            }
        }
        log.logEvent(new Event("Filtered matches"));
        return matchesWithChar;
    }

    // EFFECTS: adds wins based on the outcome
    private int addWin(boolean outcome) {
        if (outcome) {
            return 1;
        }
        return 0;
    }

    // EFFECTS: rounds up the wins and games played to two decimal places
    private double round(double wins, double games) {
        double raw;
        raw = wins / games * 100;

        return (double) Math.round(raw * 100) / 100;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Records", recordsToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray recordsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (MatchInfo m : records) {
            jsonArray.put(m.toJson());
        }

        return jsonArray;
    }
}
