package main.model;

import org.json.JSONArray;
import org.json.JSONObject;
import main.persistence.Writable;

import java.util.*;

// Represents a Combo having the name of the character,the combination of attacks and possible options after the combo
// Code regarding writing JSON file influenced by:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class Combo implements Writable {

    private String character;           // The name of character that uses the combo
    private List<String> combo;         // The list of moves in the combo
    private List<String> followUps;     // The possible options after the combo

    // EFFECTS: Sets the name of character and instantiates the combo and followups
    public Combo(String character) {
        this.character = character;
        this.combo = new ArrayList<>();
        this.followUps = new ArrayList<>();
    }

    public Combo() {
        this.character = "";
        this.combo = new ArrayList<>();
        this.followUps = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS:  adds the move into the list of moves to execute the combo
    public void addMove(String move) {
        combo.add(move);
    }

    // EFFECTS: Returns the moves of the combo
    public List<String> getComboMoves() {
        return combo;
    }

    // EFFECTS: returns the character that uses the combo
    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    // MODIFIES: this
    // EFFECTS:  adds an option after the combo
    public void addFollowUps(String followUp) {
        followUps.add(followUp);
    }

    // MODIFIES: this
    // EFFECTS:  returns true if given followup is removed; otherwise, return false
    public boolean removeFollowUp(String followUp) {
        if (followUps.contains(followUp)) {
            followUps.remove(followUp);
            return true;
        } else {
            return false;
        }
    }

    // EFFECTS: returns the possible options after the combo
    public List<String> getFollowUps() {
        return followUps;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Character", character);
        json.put("Moves", listToJson(combo));
        json.put("Follow Up", listToJson(followUps));
        return json;
    }

    // returns the given list of string as a JSON array
    private JSONArray listToJson(List<String> list) {
        JSONArray jsonArray = new JSONArray();

        for (String s: list) {
            jsonArray.put(s);
        }

        return jsonArray;
    }
}
