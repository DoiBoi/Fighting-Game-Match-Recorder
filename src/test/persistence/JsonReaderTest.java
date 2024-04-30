package test.persistence;

import main.model.MatchInfo;
import main.model.Records;
import main.persistence.JsonReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends test.persistence.JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Records r = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyRecords() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyRecords.json");
        try {
            Records r = reader.read();
            assertTrue(r.getMatchInfos().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralRecords() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralRecords.json");
        try {
            Records r = reader.read();
            assertFalse(r.getMatchInfos().isEmpty());
            List<MatchInfo> matches = r.getMatchInfos();
            assertEquals(4, matches.size());
            checkMatchWithOutCombo("Jamie", "Ryu", false, matches.get(0));
            List<String> moves = new ArrayList<>();
            moves.add("2K");
            moves.add("2D");
            List<String> followUp = new ArrayList<>();
            followUp.add("Butterfly Oki");
            checkMatchWithCombo("Anji Mito", "May", true, moves, followUp,
                    matches.get(1));
            moves.clear();
            followUp.clear();
            moves.add("A");
            moves.add("B");
            moves.add("C");
            checkMatchWithCombo("Hyde", "Enkidu", false, moves, followUp,
                    r.getMatchInfos().get(2));
            checkMatchWithOutCombo("Ragna", "Juubei", true, matches.get(3));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
