/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatransferobjects;

import static org.junit.Assert.*;
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
    public void testFirstConstructorNullFactory() {
        Site site = new Site(1, null, "area");
    }

    /**
     * Test of first constructor, with null area.
     */
    @Test(expected = NullPointerException.class)
    public void testFirstConstructorNullArea() {
        Site site = new Site(1, "factory", null);
    }

    /**
     * Test of second constructor, with null factory.
     */
    @Test(expected = NullPointerException.class)
    public void testSecondConstructorNullFactory() {
        Site site = new Site(null, "area");
    }

    /**
     * Test of second constructor, with null area.
     */
    @Test(expected = NullPointerException.class)
    public void testSecondConstructorNullArea() {
        Site site = new Site("factory", null);
    }

    /**
     * Test of equality for hashCode method, of class Site.
     */
    @Test
    public void testHashCodeEquality() {
        Site site1 = new Site(1, "factory", "area");
        Site site2 = new Site(1, "factory", "area");
        assertEquals(site1.hashCode(), site2.hashCode());
    }

    /**
     * Test of equality for equals method, of class Site.
     */
    @Test
    public void testEqualsEquality() {
        Site site1 = new Site(1, "factory", "area");
        Site site2 = new Site(1, "factory", "area");
        assertEquals(site1, site2);
    }

    /**
     * Test of inequality for equals method, of class Site. Test cases:
     * different sites, equality with null, equality with an object that does
     * not represent a site.
     */
    @Test
    public void testEqualsInequality() {
        Site site1 = new Site("factory1", "area1");
        Site site2 = new Site(1, "factory2", "area2");
        assertNotEquals(site1, site2);
        assertNotEquals(site1, null);
        assertNotEquals(site1, new Object());

    }
}
