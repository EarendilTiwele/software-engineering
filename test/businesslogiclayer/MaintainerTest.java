/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Maintainer class test.
 *
 * @author carbo
 */
public class MaintainerTest {

    private Maintainer maintainer;

    @Before
    public void setUp() {
        maintainer = new Maintainer("name", "password");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getCompetencies method (no competencies), of class Maintainer.
     */
    @Test
    public void testGetCompetenciesEmpty() {
        assertTrue(maintainer.getCompetencies().isEmpty());
    }

    /**
     * Returns a set of competencies.
     *
     * @return a set of competencies
     */
    private Set<Competency> getCompetencies() {
        Set<Competency> competencies = new HashSet<>();
        competencies.add(new Competency("description1"));
        competencies.add(new Competency("description2"));
        competencies.add(new Competency("description3"));
        return competencies;
    }

    /**
     * Adds the same competencies to maintainers.
     *
     * @param maintainer1 the first maintainer
     * @param maintainer2 the second maintainer
     */
    private void addEqualCompetencies(Maintainer maintainer1, Maintainer maintainer2) {
        for (Competency competency : getCompetencies()) {
            maintainer1.addCompetency(competency);
        }
        for (Competency competency : getCompetencies()) {
            maintainer2.addCompetency(competency);
        }
    }

    /**
     * Test of addCompetency method (one competency), of class Maintainer.
     */
    @Test
    public void testAddCompetency() {
        Competency competency = new Competency("description");
        maintainer.addCompetency(competency);
        assertTrue(maintainer.getCompetencies()
                .contains(competency));
    }

    /**
     * Test of addCompetency method (multiple competencies), of class
     * Maintainer.
     */
    @Test
    public void testAddCompetencyMultiple() {
        for (Competency competency : getCompetencies()) {
            maintainer.addCompetency(competency);
        }
        assertEquals(getCompetencies(), maintainer.getCompetencies());
    }

    /**
     * Test of removeCompetency method (no competencies), of class Maintainer.
     */
    @Test
    public void testRemoveCompetencyEmpty() {
        maintainer.removeCompetency(new Competency("description"));
        assertTrue(maintainer.getCompetencies().isEmpty());
    }

    /**
     * Test of removeCompetency method (real competency of this maintainer), of
     * class Maintainer.
     */
    @Test
    public void testRemoveCompetencyExists() {
        Set<Competency> competencies = getCompetencies();
        for (Competency competency : competencies) {
            maintainer.addCompetency(competency);
        }
        Competency competencyToRemove = competencies.iterator().next();
        competencies.remove(competencyToRemove);
        maintainer.removeCompetency(competencyToRemove);
        assertEquals(competencies, maintainer.getCompetencies());
    }

    /**
     * Test of removeCompetency method (not real competency of this maintainer),
     * of class Maintainer.
     */
    @Test
    public void testRemoveCompetencyNotExists() {
        Set<Competency> competencies = getCompetencies();
        for (Competency competency : competencies) {
            maintainer.addCompetency(competency);
        }
        Competency competencyToRemove = new Competency("fake competency");
        maintainer.removeCompetency(competencyToRemove);
        assertEquals(competencies, maintainer.getCompetencies());
    }

    /**
     * Test for equality for hashCode method (with and without competencies), of
     * class Maintainer.
     */
    @Test
    public void testHashCodeEquality() {
        Maintainer maintainer2 = new Maintainer(
                maintainer.getUsername(),
                maintainer.getPassword());
        assertEquals(maintainer.hashCode(), maintainer2.hashCode());
        addEqualCompetencies(maintainer, maintainer2);
        assertEquals(maintainer.hashCode(), maintainer2.hashCode());
    }

    /**
     * Test for equality for equals method (with and without competencies), for
     * class Maintainer.
     */
    @Test
    public void testEqualsEquality() {
        Maintainer maintainer2 = new Maintainer(
                maintainer.getUsername(),
                maintainer.getPassword());
        assertEquals(maintainer, maintainer2);
        addEqualCompetencies(maintainer, maintainer2);
        assertEquals(maintainer, maintainer2);
    }

    /**
     * Test for inequality for equals method (with and without competencies),
     * for class Maintainer.
     */
    @Test
    public void testEqualsInquality() {
        Maintainer maintainer2 = new Maintainer(
                maintainer.getUsername() + "-",
                maintainer.getPassword());
        //different username
        assertNotEquals(maintainer, maintainer2);

        //different username and equal competencies
        addEqualCompetencies(maintainer, maintainer2);
        assertNotEquals(maintainer, maintainer2);

        //different username and different competencies
        maintainer2.getCompetencies().clear();
        assertNotEquals(maintainer, maintainer2);

        //same username and different competencies
        maintainer2.setUsername(maintainer.getUsername());
        maintainer2.getCompetencies().clear();
        assertNotEquals(maintainer, maintainer2);
    }

}
