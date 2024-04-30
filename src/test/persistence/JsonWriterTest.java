package test.persistence;

import main.model.Combo;
import main.model.MatchInfo;
import main.model.Records;
import main.persistence.JsonReader;
import main.persistence.JsonWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends test.persistence.JsonTest {
    @Test
    void testWriterInvalidFile() {
        try {
            Records r = new Records();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyRecords() {
        try {
            Records r = new Records();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyRecords.json");
            writer.open();
            writer.write(r);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyRecords.json");
            r = reader.read();
            assertTrue(r.getMatchInfos().isEmpty());
            assertEquals(0, r.getMatchInfos().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralRecords() {
        try {
            Records records = new Records();
            MatchInfo m1 = new MatchInfo("Jamie", "Ryu", false);
            records.addMatchInfo(m1);

            MatchInfo m2 = new MatchInfo("Anji Mito", "May", true);
            Combo c = new Combo("Anji Mito");
            c.addMove("2K");
            c.addMove("2D");
            c.addFollowUps("Butterfly Oki");
            m2.setCombo(c);
            records.addMatchInfo(m2);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralRecords.json");
            writer.open();
            writer.write(records);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralRecords.json");
            records = reader.read();
            assertEquals(2, records.getMatchInfos().size());
            m1 = records.getMatchInfos().get(0);
            checkMatchWithOutCombo("Jamie", "Ryu", false, m1);
            m2 = records.getMatchInfos().get(1);
            List<String> moves = new ArrayList<>();
            moves.add("2K");
            moves.add("2D");
            List<String> followUp = new ArrayList<>();
            followUp.add("Butterfly Oki");
            checkMatchWithCombo("Anji Mito", "May", true, moves, followUp, m2);

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
