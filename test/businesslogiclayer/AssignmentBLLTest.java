/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import datatransferobjects.Maintainer;
import datatransferobjects.Site;
import datatransferobjects.Activity;
import datatransferobjects.Assignment;
import datatransferobjects.Procedure;
import datatransferobjects.Typology;
import datatransferobjects.PlannedActivity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static businesslogiclayer.AssignmentBO.*;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author Alfonso
 */
public class AssignmentBLLTest {

    private AssignmentBO assignmentBO;

    @Before
    public void setUp() {
        assignmentBO = new AssignmentBO();
    }

    @After
    public void tearDown() {
    }

    /**
     * Returns a set containing a single maintainer with the specified username,
     * fake password and no competencies.
     *
     * @param username the username of the maintainer
     * @return the maintainer
     */
    private Maintainer createMaintainer(String username) {
        return new Maintainer(username, "pass");
    }

    /**
     * Returns a set of maintainers with the specified usernames, fake password
     * and no competiencies.
     *
     * @return the set of maintainers
     */
    private Set<Maintainer> createMaintainers(String... usernames) {
        Set<Maintainer> maintainers = new HashSet<>();
        for (String username : usernames) {
            maintainers.add(createMaintainer(username));
        }
        return maintainers;
    }

    /**
     * Returns a fake activity with the specified parameters.
     *
     * @param id the activity id
     * @param week the week of this maintainance activity
     * @param interventionTime the estimated intervention time for this activity
     * @return
     */
    private Activity createFakeActivity(int id, int week, int interventionTime) {
        boolean interruptible = false;
        return new PlannedActivity(
                id,
                new Site("factory", "area"),
                new Typology("typology"),
                "fake description",
                interventionTime,
                interruptible,
                week,
                new Procedure("name", "smp"));
    }

    /**
     * Returns a set of fake activities with different id starting from the
     * specified one.
     *
     * @param initialId the first id
     * @param n the number of activities to generate
     * @param week the week of the activities
     * @param interventionTime the intervention time of these activities
     * @return the set of activities
     */
    private Set<Activity> getFakeActivities(int initialId, int n, int week, int interventionTime) {
        int id = initialId;
        Set<Activity> activities = new HashSet<>();
        for (int i = 0; i < n; i++) {
            Activity activity = createFakeActivity(id, week, interventionTime);
            id++;
            activities.add(activity);
        }
        return activities;
    }

    /**
     * Test of getAgenda method, of class AssignmentBLL. - 2 busy maintainers -
     * 4 maintainers: 2 busy and 2 free
     */
    @Test
    public void testGetAgendaMoreMaintainers() {
        Set<Maintainer> busyMaintainers
                = createMaintainers("Francesco", "Alfonso");
        String[] days = new String[]{
            MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
        };
        int numberOfBusyMaintainers = busyMaintainers.size();
        int week = 1;
        int id = 1;
        int interventionTime = 60;
        int numberOfActivities = 4;
        Set<Activity> activities
                = getFakeActivities(id, numberOfActivities, week, interventionTime);

        //assign activities
        Iterator<Maintainer> maintainerIterator = busyMaintainers.iterator();
        Iterator<Activity> activityIterator = activities.iterator();

        Map<Maintainer, Integer[]> expectedAgenda = new HashMap<>();
        int fullAvail = 100;
        int activityPerDay = 2;
        int expectedAvailability = (int) (100
                - interventionTime * activityPerDay / (float) WORKING_MINUTES_PER_DAY * 100);

        Set<Assignment> assignments = new HashSet<>();

        for (int i = 0; i < numberOfBusyMaintainers; i++) {
            int hour = 15;  //not important for the test
            Maintainer maintainer = maintainerIterator.next();

            //assign activityPerDay activities to this maintainer
            for (int j = 0; j < activityPerDay; j++) {
                Assignment assignment = new Assignment(
                        maintainer,
                        activityIterator.next(),
                        MONDAY,
                        hour);
                assignments.add(assignment);

            }

            //set availability
            Integer[] availability = new Integer[WORKING_DAYS_PER_WEEK];
            Arrays.fill(availability, fullAvail);
            int dayIndex = Arrays.asList(days).indexOf(MONDAY);
            availability[dayIndex] = expectedAvailability;

            //save in the expected agenda
            expectedAgenda.put(maintainer, availability);
        }

        //test for availability: no fully available maintainers
        Map<Maintainer, Integer[]> agenda
                = assignmentBO.getAgenda(assignments, expectedAgenda.keySet());
        assertEqualsAgenda(expectedAgenda, agenda);

        //now test with some fully available maintainers
        //availabilities of free maintainers
        Set<Maintainer> freeMaintainers = createMaintainers("Paperino", "Topolino");
        for (Maintainer maintainer : freeMaintainers) {
            Integer[] availability = new Integer[WORKING_DAYS_PER_WEEK];
            Arrays.fill(availability, fullAvail);
            expectedAgenda.put(maintainer, availability);
        }

        Set<Maintainer> allMaintainers = new HashSet<>();
        allMaintainers.addAll(busyMaintainers);
        allMaintainers.addAll(freeMaintainers);

        agenda = assignmentBO.getAgenda(assignments, allMaintainers);
        assertEqualsAgenda(expectedAgenda, agenda);
    }

    /**
     * Test of getAgenda method, of class AssignmentBLL. Test case: no
     * maintainers in the system
     */
    @Test
    public void testGetAgendaOfZeroMaintainer() {
        //empty data
        Set<Maintainer> maintainers = new HashSet<>();
        Set<Assignment> assignments = new HashSet<>();
        Map<Maintainer, Integer[]> agenda = assignmentBO.getAgenda(assignments, maintainers);
        assertTrue(agenda.isEmpty());
    }

    /**
     * Test of getAgenda method, of class AssignmentBLL. Test case: 4
     * maintainers and zero assignments
     */
    @Test
    public void testGetAgendaZeroAssignments() {
        Set<Maintainer> maintainers
                = createMaintainers("Toto'", "Peppino", "Franco", "Ciccio");
        //no assignments
        Set<Assignment> assignments = new HashSet<>();
        Map<Maintainer, Integer[]> agenda = assignmentBO.getAgenda(assignments, maintainers);
        Map<Maintainer, Integer[]> expectedAgenda = new HashMap<>();
        for (Maintainer maintainer : maintainers) {
            Integer[] availabilities = new Integer[WORKING_DAYS_PER_WEEK];
            //full availability
            Arrays.fill(availabilities, 100);
            expectedAgenda.put(maintainer, availabilities);
        }

        assertEqualsAgenda(expectedAgenda, agenda);
    }

    /**
     * Asserts that two agendas are equals.
     *
     * @param expectedAgenda the expected agenda
     * @param agenda the actual agenda
     */
    private void assertEqualsAgenda(Map<Maintainer, Integer[]> expectedAgenda, Map<Maintainer, Integer[]> agenda) {
        assertEquals(expectedAgenda.keySet(), agenda.keySet());
        for (Maintainer maintainer : expectedAgenda.keySet()) {
            assertArrayEquals(expectedAgenda.get(maintainer), agenda.get(maintainer));
        }
    }

    //---------------------------------------------------------------
    /**
     * Returns a full available daily agenda.
     *
     * @return the full available daily agenda
     */
    private Integer[] getFullAvailableDailyAgenda() {
        Integer[] dailyAgenda = new Integer[WORKING_HOURS_PER_DAY];
        Arrays.fill(dailyAgenda, MINUTES_PER_HOUR);
        return dailyAgenda;
    }

    /**
     * Test of getDailyAgenda method, of class AssignmentBLL. Test case: no
     * assignments in the system, week 1, on MONDAY. Full availability of a
     * maintainer.
     */
    @Test
    public void testGetDailyAgendaNoAssignments() {
        Set<Assignment> assignments = new HashSet<>();
        Maintainer maintainer = createMaintainer("Alfonso");
        int week = 1;
        Integer[] dailyAgenda = assignmentBO.getDailyAgenda(
                maintainer,
                assignments,
                week,
                MONDAY);

        assertArrayEquals(getFullAvailableDailyAgenda(), dailyAgenda);
    }

    /**
     * Test of getDailyAgenda method, of class AssignmentBLL. Test case: given a
     * maintainer ("Alfonso"), assignments to maintainers "Francesco" and
     * "Alessandro", week 1, on MONDAY. Full availability of the maintainer.
     */
    @Test
    public void testGetDailyAgendaDifferentMaintainer() {
        Maintainer francesco = createMaintainer("Francesco");
        Maintainer alfonso = createMaintainer("Alfonso");
        Maintainer alessandro = createMaintainer("Alessando");
        int initialId = 1;
        int week = 1;
        int interventionTime = 30;
        int hour = 9;
        //number of activities
        int n = 3;
        Set<Activity> activities = getFakeActivities(initialId, n, week, interventionTime);

        Iterator<Activity> iterator = activities.iterator();
        Set<Assignment> assignments = new HashSet<>();

        assignments.add(
                new Assignment(alessandro, iterator.next(), MONDAY, hour)
        );
        assignments.add(
                new Assignment(francesco, iterator.next(), MONDAY, hour)
        );
        assignments.add(
                new Assignment(francesco, iterator.next(), MONDAY, hour + 1)
        );

        Integer[] dailyAgenda = assignmentBO.getDailyAgenda(alfonso, assignments, week, MONDAY);

        assertArrayEquals(getFullAvailableDailyAgenda(), dailyAgenda);
    }

    /**
     * Test of getDailyAgenda method, of class AssignmentBLL. Test case:
     * maintainer ("Alfonso") is fully busy: activity 1: 8-12 MONDAY; activity
     * 2: 14-15 MONDAY; activity 3: 15-17 MONDAY; Maintainer "Francesco" has
     * activity: activity 4: 9-10 MONDAY
     */
    @Test
    public void testGetDailyAgendaFullBusy() {
        Maintainer alfonso = createMaintainer("Alfonso");
        Maintainer francesco = createMaintainer("Francesco");
        int id = 1;
        int week = 1;
        int interventionTime = 4 * MINUTES_PER_HOUR;
        //number of Alfonso's activities
        int n = 3;
        Activity activity1 = createFakeActivity(id, week, interventionTime);
        id++;
        interventionTime = 1 * MINUTES_PER_HOUR;
        Activity activity2 = createFakeActivity(id, week, interventionTime);
        id++;
        interventionTime = 2 * MINUTES_PER_HOUR;
        Activity activity3 = createFakeActivity(id, week, interventionTime);

        id++;
        interventionTime = 1 * MINUTES_PER_HOUR;
        Activity activity4 = createFakeActivity(id, week, interventionTime);
        Set<Assignment> assignments = new HashSet<>();

        int hour = 8;
        assignments.add(
                new Assignment(alfonso, activity1, MONDAY, hour)
        );
        assignments.add(
                new Assignment(francesco, activity4, MONDAY, hour)
        );
        hour = 14;
        assignments.add(
                new Assignment(alfonso, activity2, MONDAY, hour)
        );
        hour = 15;
        assignments.add(
                new Assignment(alfonso, activity3, MONDAY, hour)
        );

        Integer[] dailyAgenda
                = assignmentBO.getDailyAgenda(alfonso, assignments, week, MONDAY);

        Integer[] expectedDailyAgenda = new Integer[WORKING_HOURS_PER_DAY];
        Arrays.fill(expectedDailyAgenda, 0);
        assertArrayEquals(expectedDailyAgenda, dailyAgenda);
    }

    /**
     * Test of getDailyAgenda method, of class AssignmentBLL. Test case:
     * maintainer ("Alfonso") is fully busy: activity 1: 8-8:30 MONDAY; activity
     * 2: 14-14:30 MONDAY; Maintainer "Francesco" has activity: activity 4:
     * 9-9:30 MONDAY
     */
    @Test
    public void testGetDailyAgendaSimpleBusy() {
        Maintainer alfonso = createMaintainer("Alfonso");
        Maintainer francesco = createMaintainer("Francesco");
        int id = 1;
        int week = 1;
        int interventionTime = (int) (0.5f * MINUTES_PER_HOUR);
        //number of activities
        int n = 3;
        Set<Activity> activities = getFakeActivities(id, n, week, interventionTime);
        Iterator<Activity> iterator = activities.iterator();
        Set<Assignment> assignments = new HashSet<>();
        int hour = 8;
        assignments.add(
                new Assignment(alfonso, iterator.next(), MONDAY, hour)
        );
        hour = 14;
        assignments.add(
                new Assignment(alfonso, iterator.next(), MONDAY, hour)
        );
        hour = 9;
        assignments.add(
                new Assignment(francesco, iterator.next(), MONDAY, hour)
        );

        Integer[] dailyAgenda
                = assignmentBO.getDailyAgenda(alfonso, assignments, week, MONDAY);

        Integer[] expectedDailyAgenda = getFullAvailableDailyAgenda();
        for (int h : new Integer[]{H8, H14}) {
            expectedDailyAgenda[h] -= interventionTime;
        }
        assertArrayEquals(expectedDailyAgenda, dailyAgenda);

    }

    /**
     * Returns a fake assignments with an activity scheduled for week 1 and
     * specified interventio time. This activity is assigned to a fake
     * maintainer for <code>MONDAY</code> at specified hour.
     *
     * @param interventionTime the activity's intervention time
     * @param hour the assigment's hour
     * @return the fake assignment
     */
    private Assignment createFakeAssignment(int interventionTime, int hour) {
        Maintainer maintainer = createMaintainer("Francesco");
        int id = 0;
        int week = 1;
        Activity activity = createFakeActivity(id, week, interventionTime);
        String day = MONDAY;
        return new Assignment(maintainer, activity, day, hour);
    }

    /**
     * Test of validate, of class AssignmentBO. Test case: Validate an assigment
     * to a such maintainer that has initialy a fully avaible daily agenda and
     * then has some activities already assigned in the hours involved. The
     * assignament is correct since there are no conflicts.
     */
    @Test
    public void testValidateValidAssignment() {
        int interventionTime = 180;
        int hour = 8;

        Assignment assignment = createFakeAssignment(interventionTime, hour);
        Integer[] dailyAgenda = getFullAvailableDailyAgenda();

        //case 1: fully avaible daily agenda
        assertTrue(assignmentBO.validate(assignment, dailyAgenda));

        //modified daily avaibility such that the maintainer has free 20 minutes
        //at 8 o'clock and has 40 free minutes at 11 o'clock.
        int busyTimeH8 = 40;
        int busyTimeH11 = 20;

        dailyAgenda[H8] -= busyTimeH8;
        dailyAgenda[H11] -= busyTimeH11;

        //Case 2: the assignment is valid since the activiy must not be interrupted
        // to solve  other activities already assigned in those hours
        assertTrue(assignmentBO.validate(assignment, dailyAgenda));
    }

    /**
     * Test of validate, of class AssignmentBO. Test case: Validate an assigment
     * to a such maintainer that has initialy already activities that are in
     * conflicts whith the new assignment. Then try to assign an activity in an
     * hour which maintainer is fully busy.
     */
    @Test
    public void testValidateUnvalidAssignment() {
        int interventionTime = 190;
        int hour = 8;
        Assignment assignment = createFakeAssignment(interventionTime, hour);

        Integer[] dailyAgenda = getFullAvailableDailyAgenda();

        //modified daily avaibility such that the maintainer has free 20 minutes
        //at 8 o'clock and has 40 free minutes at 11 o'clock.
        int busyTimeH8 = 40;
        int busyTimeH11 = 20;

        dailyAgenda[H8] -= busyTimeH8;
        dailyAgenda[H11] -= busyTimeH11;

        //Case 1: the assignment is unvalid since the activiy must not be interrupted
        // to solve  other activities already assigned in those hours
        assertFalse(assignmentBO.validate(assignment, dailyAgenda));

        //modified daily avaibility such that the maintainer is fully busy at
        // 8 o'clock and fully available at 11 o'clock
        int freeTimeH8 = 0;
        int freeTimeH11 = 60;
        dailyAgenda[H8] = freeTimeH8;
        dailyAgenda[H11] = freeTimeH11;

        //Case 2: the assignment is unvalid since the maintainer is not available
        //at assignment's hour
        assertFalse(assignmentBO.validate(assignment, dailyAgenda));
    }

    /**
     * Test of validate, of class AssignmentBO. Test case: a maintainer doesn't
     * work extra in a day. *
     */
    @Test
    public void testValidateUnvalidExtraAssignment() {
        int extraTime = 10;
        int interventionTime = WORKING_MINUTES_PER_DAY + extraTime;
        int hour = 8;
        Assignment assignment = createFakeAssignment(interventionTime, hour);

        Integer[] dailyAgenda = getFullAvailableDailyAgenda();

        assertFalse(assignmentBO.validate(assignment, dailyAgenda));

    }

}
