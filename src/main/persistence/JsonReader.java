package main.persistence;

import main.model.Combo;
import main.model.MatchInfo;
import main.model.Records;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads Records from JSON data stored in file
// Code influenced by: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    //          throws IOException if an error occurs reading data from file
    public Records read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseRecords(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses records from JSON object and returns it
    private Records parseRecords(JSONObject jsonObject) {
        Records records = new Records();
        addMatches(records, jsonObject);
        return records;
    }

    // MODIFIES: records
    // EFFECTS:  parses matches from JSON object and adds them to records
    private void addMatches(Records records, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Records");
        for (Object json : jsonArray) {
            JSONObject nextMatch = (JSONObject) json;
            addMatch(records, nextMatch);
        }
    }

    // MODIFIES: records
    // EFFECTS: parses match from JSON object and adds it to records
    private void addMatch(Records records, JSONObject jsonObject) {
        String p1 = jsonObject.getString("Player 1");
        String p2 = jsonObject.getString("Player 2");
        Boolean outcome = jsonObject.getBoolean("Player 1 win?");
        MatchInfo match = new MatchInfo(p1, p2, outcome);
        addCombo(match, jsonObject);
        records.addMatchInfo(match);
    }

    // MODIFIES: match
    // EFFECTS:  parses combos within from the match and adds it to the match;
    //           the object can be a string which then would mean that there is no combo saved
    //           and nothing is added
    private void addCombo(MatchInfo match, JSONObject jsonObject) {
        try {
            JSONObject combo = jsonObject.getJSONObject("Combo");
            match.setCombo(createCombo(combo));
        } catch (JSONException je) {
            // if there is no combo
        }

    }


    // EFFECTS: parses the given combo and creates a combo instance from it
    private Combo createCombo(JSONObject combo) {
        Combo c = new Combo(combo.getString("Character"));
        for (Object s: combo.getJSONArray("Moves")) {
            c.addMove(s.toString());
        }
        for (Object s: combo.getJSONArray("Follow Up")) {
            c.addFollowUps(s.toString());
        }
        return c;
    }
}
