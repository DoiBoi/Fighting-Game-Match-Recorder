package test.persistence;

import main.model.MatchInfo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class JsonTest {
    protected void checkMatchWithCombo(String p1, String p2, Boolean outcome, List<String> moves, List<String> followUp,
                                       MatchInfo match) {
        assertEquals(p1, match.getCharacter(1));
        assertEquals(p2, match.getCharacter(2));
        assertEquals(outcome, match.getOutcome());
        if (!(match.getCombo() == null)) {
            assertEquals(moves, match.getCombo().getComboMoves());
            assertEquals(followUp, match.getCombo().getFollowUps());
        }
    }

    protected void checkMatchWithOutCombo(String p1, String p2, Boolean outcome, MatchInfo match) {
        assertEquals(p1, match.getCharacter(1));
        assertEquals(p2, match.getCharacter(2));
        assertEquals(outcome, match.getOutcome());
        assertNull(match.getCombo());
    }
}
