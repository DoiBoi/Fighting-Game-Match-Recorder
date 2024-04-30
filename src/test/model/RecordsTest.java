package test.model;

import main.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecordsTest {

    Records testRecords;
    MatchInfo m1;
    MatchInfo m2;
    MatchInfo m3;
    MatchInfo m4;
    MatchInfo m5;

    @BeforeEach
    void setUp(){
        testRecords = new Records();
        m1 = new MatchInfo("Anji Mito", "Potemkin", false);
        m2 = new MatchInfo("Jamie", "Marissa", false);
        m3 = new MatchInfo("Jamie", "Marissa", true);
        m4 = new MatchInfo("Jamie", "Marissa", false);
        m5 = new MatchInfo("Jamie", "Jamie", true);
    }

    @Test
    void testConstructor(){
        List<MatchInfo> records = testRecords.getMatchInfos();
        assertTrue(records.isEmpty());
    }

    @Test
    void testAddMatchInfo(){
        testRecords.addMatchInfo(m1);
        List<MatchInfo> records = new ArrayList<>();
        records.add(m1);
        assertEquals(records, testRecords.getMatchInfos());
    }

    @Test
    void testAddMultipleMatches(){
        testRecords.addMatchInfo(m1);
        testRecords.addMatchInfo(m2);
        List<MatchInfo> records = new ArrayList<>();
        records.add(m1);
        records.add(m2);
        assertEquals(records, testRecords.getMatchInfos());
    }

    @Test
    void testRemoveMatches(){
        testRecords.addMatchInfo(m1);
        testRecords.addMatchInfo(m2);
        testRecords.addMatchInfo(m4);
        assertTrue(testRecords.removeMatchInfo(m2));
        List<MatchInfo> records = new ArrayList<>();
        records.add(m1);
        records.add(m4);
        assertEquals(records, testRecords.getMatchInfos());
    }

    @Test
    void testRemoveMultipleMatches(){
        testRecords.addMatchInfo(m1);
        testRecords.addMatchInfo(m2);
        testRecords.addMatchInfo(m3);
        assertTrue(testRecords.removeMatchInfo(m1));
        assertTrue(testRecords.removeMatchInfo(m3));
        List<MatchInfo> records = new ArrayList<>();
        records.add(m2);
        assertEquals(records, testRecords.getMatchInfos());
    }

    @Test
    void testRemoveNonExistentMatch(){
        testRecords.addMatchInfo(m1);
        testRecords.addMatchInfo(m2);
        assertFalse(testRecords.removeMatchInfo(m3));
        List<MatchInfo> records = new ArrayList<>();
        records.add(m1);
        records.add(m2);
        assertEquals(records, testRecords.getMatchInfos());
    }

    @Test
    void testGetWinRateWithNoMatches(){
        assertEquals(0, testRecords.getWinRate("Jamie"));
    }

    @Test
    void testGetWinRateWithOneEntry(){
        testRecords.addMatchInfo(m1);
        assertEquals(0, testRecords.getWinRate("Anji Mito"));
    }

    @Test
    void testGetWinRateWithOneWinAndOneLoss(){
        testRecords.addMatchInfo(m2);
        testRecords.addMatchInfo(m3);
        assertEquals(50, testRecords.getWinRate("Jamie"));
    }

    @Test
    void testGetWinRateWithNoMatchingName(){
        testRecords.addMatchInfo(m2);
        testRecords.addMatchInfo(m3);
        assertEquals(0, testRecords.getWinRate("Anji Mito"));
    }

    @Test
    void testGetWinRateFromPlayer2(){
        testRecords.addMatchInfo(m2);
        testRecords.addMatchInfo(m3);
        testRecords.addMatchInfo(m4);
        assertEquals(66.67, testRecords.getWinRate("Marissa"));
    }

    @Test
    void testGetWinRateFromMirrorMatch() {
        testRecords.addMatchInfo(m4);
        assertEquals(0, testRecords.getWinRate("Jamie"));
        testRecords.addMatchInfo(m3);
        assertEquals(50, testRecords.getWinRate("Jamie"));
        testRecords.addMatchInfo(m5);
        assertEquals(50, testRecords.getWinRate("Jamie"));
    }

    @Test
    void testFilterMatchWithNoMatchingName() {
        testRecords.addMatchInfo(m1);
        assertTrue(testRecords.filterMatches("Jamie").isEmpty());
    }

    @Test
    void testFilterMatchWithOneMatchingName(){
        testRecords.addMatchInfo(m1);
        testRecords.addMatchInfo(m2);
        assertEquals(1, testRecords.filterMatches("Jamie").size());
        assertEquals(m2, testRecords.filterMatches("Jamie").get(0));
    }

    @Test
    void testFilterMatchWithMultipleMatchingName() {
        testRecords.addMatchInfo(m1);
        testRecords.addMatchInfo(m2);
        testRecords.addMatchInfo(m3);
        List<MatchInfo> matches = testRecords.filterMatches("Jamie");
        assertEquals(2, matches.size());
        assertEquals(m2, matches.get(0));
        assertEquals(m3, matches.get(1));
    }

    @Test
    void testFilterMatchFromPlayer2() {
        testRecords.addMatchInfo(m1);
        testRecords.addMatchInfo(m2);
        testRecords.addMatchInfo(m3);
        testRecords.addMatchInfo(m4);
        List<MatchInfo> matches = testRecords.filterMatches("Marissa");
        assertEquals(3, matches.size());
        assertEquals(m2, matches.get(0));
        assertEquals(m3, matches.get(1));
        assertEquals(m4, matches.get(2));
    }
}
