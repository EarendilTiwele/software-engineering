/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;


import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;


/**
 * PlannerActivity test class
 * 
 * @author Alfonso
 */
public class PlannedActivityTest {
    
    public PlannedActivity planned;
    
    @Before
    public void setUp(){
        planned=new PlannedActivity(0, 
                new Site("Factory","Area"), new Typology("Typology"),
                "description", 0, true, 1, new Procedure("name","smp"));
       
        
    }

    /**
     * Test of constructor, with workspaceNotes null
     */
    @Test (expected = NullPointerException.class)
    public void testConstructorWorkspaceNull(){
        PlannedActivity plannedActivity = new PlannedActivity(0, 
                new Site("factory", "area"), new Typology("typology"),
                "desctiption", 0, true, 1, new Procedure("name", "smp"),null);
        
    }
    
    /**
     * Test of constructor, with procedure null
     */
    @Test (expected = NullPointerException.class)
    public void testConstructorProcedureNull(){
        PlannedActivity plannedActivity = new PlannedActivity(0, 
                new Site("factory", "area"),new Typology("typology"),
                "desctiption", 0, true, 1, null,"workspaceNotes");
        
    }
    
    /**
     * Test of constructor, with description null
     */
    @Test (expected = NullPointerException.class)
    public void testConstructorDescriptionNull(){
        PlannedActivity plannedActivity = new PlannedActivity(0, 
                new Site("factory", "area"), new Typology("typology"), 
                null, 0, true, 1, new Procedure("name", "smp"),"workspaceNotes");
        
    }
    
    /**
     * Test of constructor, with site null
     */
    @Test (expected = NullPointerException.class)
    public void testConstructorSiteNull(){
        PlannedActivity plannedActivity = new PlannedActivity(0, null,
                new Typology("typology"), "desctiption", 0, true, 1,
                new Procedure("name", "smp"),"workspaceNotes");
        
    }
    
    /**
     * Test of constructor, with typology null
     */
    @Test (expected = NullPointerException.class)
    public void testConstructorTypologyNull(){
        PlannedActivity plannedActivity = new PlannedActivity(0, 
                new Site("factory", "area"),null, "descriton", 0, true, 1, 
                new Procedure("name", "smp"),"workspaceNotes");
        
    }
    
    /**
     * Test of constructor with week equal to 0
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorWeekEqualZero(){
        int week=0;
        PlannedActivity plannedActivity =new PlannedActivity(0, 
                new Site("factory", "area"),new Typology("typology"), "descriton", 0, true, week, 
                new Procedure("name", "smp"),"workspaceNotes");
    }
    
    /**
     * Test of constructor with week equal to 53
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorWeekEqualFiftyThree(){
        int week=53;
        PlannedActivity plannedActivity =new PlannedActivity(0, 
                new Site("factory", "area"),new Typology("typology"), "descriton", 0, true, week, 
                new Procedure("name", "smp"),"workspaceNotes");
    }
    
    
    /**
     * Test addMaterial without insert material
     */
    @Test
    public void testAddMaterialEmpty(){
        assertTrue(planned.getMaterials().isEmpty());
    }   
    
    /**
     * Test addMaterial, with add a material
     */
    @Test
    public void testAddMaterialOneMaterial(){
        String material="material";
        planned.addMaterials(material);
        
        assertEquals(planned.getMaterials().size(), 1);
        
        assertTrue(planned.getMaterials().contains(material));
        
    }
 
    /**
     * Test addMaterial, with add multiple material
     */
    @Test
    public void testAddMaterialMultipleMaterial(){
        String [] materials = new String []{"material1","material2","material3"};
        
        for (int i = 0; i<materials.length;i++){
            planned.addMaterials(materials[i]);
        }
        
        assertEquals(planned.getMaterials().size(), materials.length);
        
        for (int i = 0; i<materials.length;i++){
            assertTrue(planned.getMaterials().contains(materials[i]));
        }
        
    }
    
    /**
    * Test the equality for equals method of Tyoplogy class.
    */
    @Test
    public void testPlannedActivityEquals(){
        PlannedActivity planned2= new PlannedActivity(planned.getId(),
                planned.getSite(), planned.getTipology(), planned.getDescription(),
                planned.getInterventionTime(), planned.isInterruptible(), 
                planned.getWeek(), planned.getProcedure());
        assertEquals(planned, planned2);
    }
    
    /**
    * Test the inequality for equals method of Tyoplogy class.
    */
    @Test
    public void testPlannedActivityNotEquals(){
        int different_week = planned.getWeek()+1;
        PlannedActivity different_planned = new PlannedActivity(planned.getId(),
                planned.getSite(), planned.getTipology(), planned.getDescription(),
                planned.getInterventionTime(), planned.isInterruptible(), 
                different_week, planned.getProcedure());
        assertNotEquals(planned, different_week);
        
    }
    
    
}
