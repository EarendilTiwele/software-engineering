/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

/**
 * Typology for maintenance activities. 
 * 
 * @author Alfonso
 */
public class Typology {
    private final int id;
    private final String name;
    private static final int DEFAULT_ID = -1;

    /**
     * Constructs the typology with the specified id and typology name.
     * 
     * @param id     the id of this typology
     * @param name   the name of this typology
     */
    public Typology(int id, String name) {
        if (name == null){
            throw new NullPointerException("name must not be null");
        }
        this.id = id;
        this.name = name;
    }

    
    /**
     * Constructs the typology with the specified name.
     * The id associated with this typology will not be significant.
     * 
     * @param name the name of this typology
     */
    public Typology(String name) {
        this(DEFAULT_ID,name);
    }

    
    /**
     * Returns the id associated with this typology.
     * 
     * @return the typology id
     */
    public int getId() {
        return id;
    }

    
    /**
     * Returns the name associated with this typology.
     * 
     * @return the typology name
     */
    public String getName() {
        return name;
    }
    
    
    
   
    
    
}
