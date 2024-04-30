package test.model;

import main.model.Combo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ComboTest {

    Combo testCombo1;
    Combo testCombo2;
    List<String> moves;
    String m1;
    String m2;
    String m3;
    String m4;

    @BeforeEach
    void setUp(){
        testCombo2 = new Combo();
        testCombo1 = new Combo("Guile");
        moves = new ArrayList<>();
        m1 = "2LK";
        m2 = "2LP";
        m3 = "5LK";
        m4 = "[2]8HK";
    }

    @Test
    void testMakeCombo(){
        testCombo1.addMove(m1);
        testCombo1.addMove(m2);
        moves.add(m1);
        moves.add(m2);
        assertEquals(moves, testCombo1.getComboMoves());
    }

    @Test
    void testConstructor(){
        assertEquals("Guile", testCombo1.getCharacter());
        assertEquals("", testCombo2.getCharacter());
    }

    @Test
    void testSetCharacter() {
        testCombo2.setCharacter("Ryu");
        assertEquals("Ryu", testCombo2.getCharacter());
    }

    @Test
    void testAddFollowUp(){
        initCombo();
        List<String> followUps = new ArrayList<>();
        followUps.add("Okizeme");
        followUps.add("Light Confirm");
        testCombo1.addFollowUps("Okizeme");
        testCombo1.addFollowUps("Light Confirm");
        assertEquals(followUps, testCombo1.getFollowUps());

    }

    @Test
    void testRemoveFollowUp() {
        initCombo();
        testCombo1.addFollowUps("Okizeme");
        testCombo1.addFollowUps("Light Confirm");
        List<String> followUps = new ArrayList<>();
        assertTrue(testCombo1.removeFollowUp("Light Confirm"));
        followUps.add("Okizeme");
        assertEquals(followUps, testCombo1.getFollowUps());
    }

    @Test
    void testRemoveNonExistentFollowUp(){
        initCombo();
        testCombo1.addFollowUps("Okizeme");
        assertFalse(testCombo1.removeFollowUp("Light Confirm"));
        assertEquals("Okizeme", testCombo1.getFollowUps().get(0));
    }

    private void initCombo(){
        testCombo1.addMove(m1);
        testCombo1.addMove(m2);
        testCombo1.addMove(m3);
        testCombo1.addMove(m4);
    }
}
