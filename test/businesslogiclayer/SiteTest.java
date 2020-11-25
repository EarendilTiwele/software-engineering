/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import org.junit.Test;

/**
 * Site class test.
 * 
 * @author carbo
 */
public class SiteTest {

    /**
     * Test of first constructor, with null factory.
     */
    @Test(expected = NullPointerException.class)
    public void testFirstConstructorNullFactory(){
        Site site = new Site(1, null, "area");
    }
    
    /**
     * Test of first constructor, with null area.
     */
    @Test(expected = NullPointerException.class)
    public void testFirstConstructorNullArea(){
        Site site = new Site(1, "factory", null);
    }
    
    /**
     * Test of second constructor, with null factory.
     */
    @Test(expected = NullPointerException.class)
    public void testSecondConstructorNullFactory(){
        Site site = new Site(null, "area");
    }
    
    /**
     * Test of second constructor, with null area.
     */
    @Test(expected = NullPointerException.class)
    public void testSecondConstructorNullArea(){
        Site site = new Site("factory", null);
    }
    
    
}
