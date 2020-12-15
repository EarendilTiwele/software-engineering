/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatransferobjects;

import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Procedure class test.
 *
 * @author carbo
 */
public class ProcedureTest {

    private Procedure procedure;

    @Before
    public void setUp() {
        procedure = new Procedure("procedure", "./smp1.pdf");
    }

    /**
     * Test of first constructor, with null name.
     */
    @Test(expected = NullPointerException.class)
    public void testFirstConstructorNullName() {
        Procedure procedure = new Procedure(1, null, "");
    }

    /**
     * Test of first constructor, with null SMP path.
     */
    @Test(expected = NullPointerException.class)
    public void testFirstConstructorNullSMP() {
        Procedure procedure = new Procedure(1, "procedure name", null);
    }

    /**
     * Test of second constructor, with null name.
     */
    @Test(expected = NullPointerException.class)
    public void testSecondConstructorNullName() {
        Procedure procedure = new Procedure(null, "");
    }

    /**
     * Test of second constructor, with null SMP path.
     */
    @Test(expected = NullPointerException.class)
    public void testSecondConstructorNullSMP() {
        Procedure procedure = new Procedure("procedure name", null);
    }

    /**
     * Test of getCompetencies method (no competencies), of class Procedure.
     */
    @Test
    public void testGetCompetenciesEmpty() {
        Set<Competency> competencies = procedure.getCompetencies();
        assertTrue(competencies.isEmpty());
    }

    /**
     * Test of addCompetency method (one competency), of class Procedure.
     */
    @Test
    public void testAddCompetencyOne() {
        String description = "description1";
        Competency competency = new Competency(description);

        procedure.addCompetency(competency);

        Set<Competency> competencies = procedure.getCompetencies();

        Set<Competency> expectedCompetencies = new HashSet<>();
        expectedCompetencies.add(new Competency(description));

        assertEquals(expectedCompetencies, competencies);
    }

    /**
     * Test of addCompetency method (multiple competencies), of class Procedure.
     */
    @Test
    public void testAddCompetencyMultiple() {
        String description1 = "description1";
        String description2 = "description2";
        String description3 = "description3";

        Competency competency1 = new Competency(description1);
        Competency competency2 = new Competency(description2);
        Competency competency3 = new Competency(description3);

        procedure.addCompetency(competency1);
        procedure.addCompetency(competency2);
        procedure.addCompetency(competency3);

        Set<Competency> competencies = procedure.getCompetencies();

        assertEquals(3, competencies.size());
        assertTrue(competencies.contains(new Competency(description1)));
        assertTrue(competencies.contains(new Competency(description2)));
        assertTrue(competencies.contains(new Competency(description3)));

    }

    /**
     * Returns a set of competencies.
     *
     * @return a set of competencies
     */
    private Set<Competency> getCompetences() {
        Set<Competency> competencies = new HashSet<>();
        competencies.add(new Competency("description1"));
        competencies.add(new Competency("description2"));
        competencies.add(new Competency("description3"));
        return competencies;
    }

    /**
     * Adds the same competencies to procedures.
     *
     * @param procedure1 the first procedure
     * @param procedure2 the second procedure
     */
    private void addEqualCompetencies(Procedure procedure1, Procedure procedure2) {
        for (Competency competency : getCompetences()) {
            procedure1.addCompetency(competency);
        }
        for (Competency competency : getCompetences()) {
            procedure2.addCompetency(competency);
        }
    }

    /**
     * Test for equality for hashCode method (with and without competencies), of
     * class Procedure.
     */
    @Test
    public void testHashCodeEquality() {
        Procedure procedure1 = new Procedure(1, "procedure1", "smp1.pdf");
        Procedure procedure2 = new Procedure(1, "procedure1", "smp1.pdf");
        assertEquals(procedure1.hashCode(), procedure2.hashCode());
        addEqualCompetencies(procedure1, procedure2);
        assertEquals(procedure1.hashCode(), procedure2.hashCode());
    }

    /**
     * Test for equality for equals method (with and without competencies), for
     * class Procedure.
     */
    @Test
    public void testEqualsEquality() {
        Procedure procedure1 = new Procedure(1, "procedure1", "smp1.pdf");
        Procedure procedure2 = new Procedure(1, "procedure1", "smp1.pdf");
        assertEquals(procedure1, procedure2);
        addEqualCompetencies(procedure1, procedure2);
        assertEquals(procedure1, procedure2);
    }

    /**
     * Test for inequality for equals method (with and without competencies),
     * for class Procedure. Test cases: different procedures, equality with
     * null, equality with an object that does not represent a procedure.
     */
    @Test
    public void testEqualsInequality() {
        Procedure procedure1 = new Procedure("procedure1", "smp1.pdf");
        Procedure procedure2 = new Procedure(1, "procedure2", "smp1.pdf");
        assertNotEquals(procedure1, procedure2);
        addEqualCompetencies(procedure1, procedure2);
        assertNotEquals(procedure1, procedure2);
        assertNotEquals(procedure1, null);
        assertNotEquals(procedure1, new Object());

    }
}
