package test.model;

import main.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchInfoTest {

    MatchInfo testMatchInfo;
    Combo combo;

    @BeforeEach
    void setUp(){
        testMatchInfo = new MatchInfo("Ryu", "Ken", true);
        combo = new Combo("Ryu");
    }

    @Test
    void testConstructor() {
        assertEquals("Ryu", testMatchInfo.getCharacter(1));
        assertEquals("Ken", testMatchInfo.getCharacter(2));
        assertTrue(testMatchInfo.getOutcome());
    }

    @Test
    void testSetCharacter2() {
        testMatchInfo.setCharacter(2, "Rashid");
        assertEquals("Rashid", testMatchInfo.getCharacter(2));
    }

    @Test
    void testSetCharacter1() {
        testMatchInfo.setCharacter(1, "Manon");
        assertEquals("Manon", testMatchInfo.getCharacter(1));
    }

    @Test
    void testSetOutcome() {
        testMatchInfo.setOutcome(false);
        assertFalse(testMatchInfo.getOutcome());
    }

    @Test
    void testSetCombo(){
        initCombo(combo);
        testMatchInfo.setCombo(combo);
        assertEquals(combo, testMatchInfo.getCombo());
    }

    private void initCombo(Combo c){
        c.addMove("2HP");
        c.addMove("236KK");
        c.addMove("623HP");
        c.addFollowUps("Fireball Setup");
    }
}