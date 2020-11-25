/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;


import org.junit.Test;

/**
 * Typology class test
 * 
 * @author Alfonso
 */
public class TypologyTest {
    

    /**
     * Test of first constructor, with null name.
    */
    @Test(expected = NullPointerException.class)
    public void testFirstConstructorNull() {
        Typology typology = new Typology(null);
    }

    /**
     * Test of second constructor, with null name.
     */
    @Test(expected = NullPointerException.class)
    public void testSecondConstructorNull() {
        Typology typology = new Typology(1, null);
    }

    
}
