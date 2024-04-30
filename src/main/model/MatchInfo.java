package main.model;

import org.json.JSONObject;
import main.persistence.Writable;

// Represents a match played with characters used for player 1 and player 2, the outcome
// of the match and a notable combo from that match
// Code regarding writing JSON file influenced by:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class MatchInfo implements Writable {

    private String player1;     // the character used for player 1
    private String player2;     // the character used for player 2
    private boolean outcome;    // the outcome of the match; whether player 1 has won or lost
    private Combo combo;        // the combo used

    // EFFECTS: creates a match account with player 1 and player 2 with the game's outcome;
    public MatchInfo(String p1, String p2, Boolean outcome) {
        this.player1 = p1;
        this.player2 = p2;
        this.outcome = outcome;
        this.combo = null;
    }

    // REQUIRES: int player to be either 1 or 2
    // MODIFIES: this
    // EFFECTS:  sets the name of the character based on which side the character was on
    //           (whether they are player 1 or 2)
    public void setCharacter(int player, String name) {
        if (player == 1) {
            this.player1 = name;
        } else {
            this.player2 = name;
        }
    }

    // REQUIRES: int player is either 1 or 2
    // EFFECTS:  returns the character based on which side they are on
    public String getCharacter(int player) {
        if (player == 1) {
            return player1;
        } else {
            return player2;
        }
    }

    // MODIFIES: this
    // EFFECTS:  sets the outcome of the match
    public void setOutcome(boolean outcome) {
        this.outcome = outcome;
    }

    // EFFECTS: returns the outcome of the match
    public boolean getOutcome() {
        return outcome;
    }

    // MODIFIES: this
    // EFFECTS:  sets the combo in the match
    public void setCombo(Combo c) {
        this.combo = c;
    }

    // MODIFIES: this
    // EFFECTS: gets the combo from the match
    public Combo getCombo() {
        return combo;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("Player 1", player1);
        json.put("Player 2", player2);
        json.put("Player 1 win?", outcome);
        comboToJson(json);

        return json;
    }

    // EFFECTS: converts the combo in the match to a JSON object;
    //          if there is no combo, then put "null" instead
    private void comboToJson(JSONObject json) {
        if (combo == null) {
            json.put("Combo", "null");
        } else {
            json.put("Combo", combo.toJson());
        }
    }
}
