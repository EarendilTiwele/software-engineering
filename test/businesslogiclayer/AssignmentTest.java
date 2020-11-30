/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Assignment class test.
 *
 * @author carbo
 */
public class AssignmentTest {

    private Assignment assignment;

    @Before
    public void setUp() {
        Maintainer maintainer = new Maintainer("maintainer", "password");
        int activityId = 1;
        int interventionTime = 10;
        int week = 3;
        boolean interruptible = true;
        Activity activity = new PlannedActivity(
                activityId,
                new Site("factory", "area"),
                new Typology("typology"),
                "description",
                interventionTime,
                interruptible,
                week,
                new Procedure("procedure name", "smp.pdf"));

        String day = "mon";
        int hour = 15;
        assignment = new Assignment(maintainer, activity, day, hour);
    }

    @After
    public void tearDown() {
    }

    
    // create a copy of an assignment
    private Assignment copyAssignment(Assignment assig) {
        Maintainer maintainer = assig.getMaintainer();
        Activity activity = assig.getActivity();
        String day = assig.getDay();
        int hour = assig.getHour();
        return new Assignment(maintainer, activity, day, hour);
    }

    /**
     * Test for equality of hashCode method, of class Assignment.
     */
    @Test
    public void testHashCode() {
        assertEquals(assignment.hashCode(), copyAssignment(assignment).hashCode());
    }

    /**
     * Test for equality of equals method, of class Assignment.
     */
    @Test
    public void testEquals() {
        assertEquals(assignment, copyAssignment(assignment));
    }

    /**
     * Test for inequality of equals method, of class Assignment.
     */
    @Test
    public void testNotEquals() {
        //different day
        Assignment assignment2 = copyAssignment(assignment);
        assignment2.setDay(assignment2.getDay() + "-");
        assertNotEquals(assignment, assignment2);

        //different hour
        assignment2 = copyAssignment(assignment);
        assignment2.setHour(assignment2.getHour() + 1);
        assertNotEquals(assignment, assignment2);

        //different maintainer
        assignment2 = new Assignment(
                new Maintainer("fake", "fake"),
                assignment.getActivity(), assignment.getDay(),
                assignment.getHour());
        assertNotEquals(assignment, assignment2);

        //different activty
        Activity originalActivity = assignment.getActivity();
        Activity differentActivity = new PlannedActivity(
                originalActivity.getId() + 1,
                originalActivity.getSite(),
                originalActivity.getTipology(),
                originalActivity.getDescription(),
                originalActivity.getInterventionTime(),
                originalActivity.isInterruptible(),
                originalActivity.getWeek(),
                originalActivity.getProcedure());

        assignment2 = new Assignment(
                assignment.getMaintainer(),
                differentActivity, assignment.getDay(),
                assignment.getHour());
        assertNotEquals(assignment, assignment2);
    }

}
