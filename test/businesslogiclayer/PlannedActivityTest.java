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
                "description", 0, true, 0, new Procedure("name","smp"));
       
        
    }

    /**
     * Test of constructor, with workspaceNotes null
     */
    @Test (expected = NullPointerException.class)
    public void testConstructorWorkspaceNull(){
        PlannedActivity plannedActivity = new PlannedActivity(0, 
                new Site("factory", "area"), new Typology("typology"),
                "desctiption", 0, true, 0, new Procedure("name", "smp"),null);
        
    }
    
    /**
     * Test of constructor, with procedure null
     */
    @Test (expected = NullPointerException.class)
    public void testConstructorProcedureNull(){
        PlannedActivity plannedActivity = new PlannedActivity(0, 
                new Site("factory", "area"),new Typology("typology"),
                "desctiption", 0, true, 0, null,"workspaceNotes");
        
    }
    
    /**
     * Test of constructor, with description null
     */
    @Test (expected = NullPointerException.class)
    public void testConstructorDescriptionNull(){
        PlannedActivity plannedActivity = new PlannedActivity(0, 
                new Site("factory", "area"), new Typology("typology"), 
                null, 0, true, 0, new Procedure("name", "smp"),"workspaceNotes");
        
    }
    
    /**
     * Test of constructor, with site null
     */
    @Test (expected = NullPointerException.class)
    public void testConstructorSiteNull(){
        PlannedActivity plannedActivity = new PlannedActivity(0, null,
                new Typology("typology"), "desctiption", 0, true, 0,
                new Procedure("name", "smp"),"workspaceNotes");
        
    }
    
    /**
     * Test of constructor, with typology null
     */
    @Test (expected = NullPointerException.class)
    public void testConstructorTypologyNull(){
        PlannedActivity plannedActivity = new PlannedActivity(0, 
                new Site("factory", "area"),null, "descriton", 0, true, 0, 
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
}
