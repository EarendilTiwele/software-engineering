/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;


import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Typology class test
 * 
 * @author Alfonso
 */
public class TypologyTest {
    
    /**
     * Create an array of n different Typology object.
     * @param n the number of Typology objet to create
     * @return the array of n Typology object
     */
    private Typology[] createNTypologies(int n){
        Typology [] typologies = new Typology[n];
        for (int i =0;i<n;i++){
            String name="Typology"+i;
            typologies[i]= new Typology(name);               
        }
        return typologies;
    }
          
    /**
     * Test of first constructor, with null name.
    */
    @Test(expected = IllegalArgumentException.class)
    public void testFirstConstructorNull() {
        Typology typology = new Typology(null);
    }

    /**
     * Test of second constructor, with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSecondConstructorNull() {
        Typology typology = new Typology(1, null);
    }
    
   /**
    * Test the inequality for equals method of Typology class.
    */
   @Test
   public void testTypologyNotEquals(){
       int n=2;
       Typology [] typologies = createNTypologies(n);
       assertNotEquals(typologies[0], typologies[1]);
   }
   
   /**
    * Test the equality for equals method of Tyoplogy class.
    */
   @Test
   public void testTypologyEquals(){
       int n=1;
       
       Typology typology1= createNTypologies(n)[0]; 
       
       // typology2 has the same name of typology1 so it is equal to typology2
       Typology typology2= new Typology(typology1.getName()); 
       assertEquals(typology1, typology2);
       
   }
}
