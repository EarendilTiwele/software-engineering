/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alfonso
 */
public class AssignmentBLLTest {
    
    public AssignmentBLLTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    private Set<Maintainer> getOneMaintainer(String username){
        Set<Maintainer> maintainers = new HashSet<>();
        Maintainer maintainer1 = new Maintainer(username, "pass");
        return maintainers;
    }

    private Set<Maintainer> getThreeMaintainers(){
        Set<Maintainer> maintainers = new HashSet<>();
        maintainers.addAll(getOneMaintainer("Pippo"));
        maintainers.addAll(getOneMaintainer("Alfonso"));
        maintainers.addAll(getOneMaintainer("Pluto"));
        //Maintainer maintainer1 = new Maintainer("Pippo", "pass");
        //Maintainer maintainer2 = new Maintainer ("Pluto","pass");
        //Maintainer maintainer3 = new Maintainer ("Alfo","pass");
        
        
        
        return maintainers;
    }

    
    private Set<Assignment> getAssignments(Set<Maintainer> maintainers, int [] numberOfAssignment){
        Set<Assignment> assignments = new HashSet<>();
        int numMaintainer=0;
        String [] days= new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        for(Maintainer maintainer : maintainers){
            for(int i = 0;i<numberOfAssignment[numMaintainer];i++){
                Activity activity = new PlannedActivity(i, 
                        new Site("Area","Department"), new Typology("typology"),
                        "", 60, true, 4, new Procedure("procedure","smp"));
                Assignment assigment = new Assignment(maintainer, activity, days[i], 9);
                assignments.add(assigment);
            }
            numMaintainer++;
        }
        
        return assignments;
        
    }
    /**
     * Test of getAgenda method, of class AssignmentBLL.
     */
    @Test
    public void testGetAgendaOfThreeMaintainers() {
        System.out.println("getAgenda");
        Set<Maintainer> maintainers = getThreeMaintainers();
        int [] numberOfAssignment = {1,0,0,0,0,0,0};
        
        // Ad ogni mantainer assegno un'attività il lunedi di 60 minuti
        Set<Assignment> assignments = getAssignments(maintainers, numberOfAssignment);
        
        Map<Maintainer,Integer[]> expectedAgenda = new HashMap<>();
        
        for (Maintainer maintainer : maintainers){
            Integer [] disponibilities = new Integer[]{85,100,100,100,100,100,100};
            expectedAgenda.put(maintainer,disponibilities );
        }
        
        AssignmentBLL instance = new AssignmentBLL();
        Map expResult = expectedAgenda;
        Map result = instance.getAgenda(assignments, maintainers);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Test of getAgenda method, of class AssignmentBLL.
     */
    @Test
    public void testGetAgendaOfZeroMaintainer() {
        Set<Maintainer> maintainers = new HashSet<>();
        int [] numberOfAssignment = {0,0,0,0,0,0,0};
        
        // Ad ogni mantainer assegno un'attività il lunedi di 60 minuti
        Set<Assignment> assignments = getAssignments(maintainers, numberOfAssignment);
        
        Map<Maintainer,Integer[]> expectedAgenda = new HashMap<>();
        
        for (Maintainer maintainer : maintainers){
            Integer [] disponibilities = new Integer[]{85,100,100,100,100,100,100};
            expectedAgenda.put(maintainer,disponibilities );
        }
        
        AssignmentBLL instance = new AssignmentBLL();
        Map expResult = expectedAgenda;
        Map result = instance.getAgenda(assignments, maintainers);
        assertEquals(expResult, result);
        
    }
}
