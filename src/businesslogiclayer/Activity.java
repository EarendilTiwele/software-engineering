/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;


import java.util.HashSet;
import java.util.Set;

/**
 * Activity is the abstract base class for all maintenance activities.
 * An Activity object contains all the necessary information for a generic 
 * activity. These includes: 
 * <ul>
 * <li>The activity id 
 * <li>The site where the activity is performed
 * <li>The typology of the activity
 * <li>The description of activity
 * <li>The estimeted intervention time of the activity
 * <li>The possibility of being interrupted
 * <li>The list of associated materials (optional)
 * <li>The week the activity is scheduled for
 * <li>The associated procedure
 * <li>The workspace notes (optional)
 * </ul>
 * 
 * 
 * @author Alfonso
 */
public abstract class Activity {

    private int id;
    private Site site;
    private Typology tipology;
    private String description;
    private int interventionTime;
    private boolean interruptible;
    private Set<String> materials = new HashSet<String>();
    private int week;
    private Procedure procedure;
    private String workspaceNotes;
    public enum ActivityCategory{PLANNED,EWO,EXTRA};
    
    /**
     * Constructs an activity with the specified id, site, typology, description,
     * interventionTime, interruptible, week, procedure, workspaceNotes.
     * 
     * @param id                the activity id
     * @param site              the site where the activity is performed
     * @param tipology          the typology of the activity
     * @param description       the description of activity
     * @param interventionTime  the estimeted intervention time of the activity
     * @param interruptible     the possibility of being interrupted
     * @param week              the week the activity is scheduled for
     * @param procedure         the associated procedure
     * @param workspaceNotes    the workspace notes 
     */
    
    public Activity(int id, Site site, Typology tipology, String description, 
                    int interventionTime, boolean interruptible, int week, 
                    Procedure procedure, String workspaceNotes) {
        if(site == null || tipology == null || description == null || 
                procedure == null || workspaceNotes == null){
            throw new NullPointerException("The parameters must not be null");
        }
        this.id = id;
        this.site = site;
        this.tipology = tipology;
        this.description = description;
        this.interventionTime = interventionTime;
        this.interruptible = interruptible;
        this.week = week;
        this.procedure = procedure;
        this.workspaceNotes = workspaceNotes;
    }

    
    /**
     * Constructs an activity with the specified id, site, typology, description,
     * interventionTime, interruptible, week, procedure, workspaceNotes.
     * The workspaceNotes associated with this typology will not be significant.
     * 
     * @param id                the activity id
     * @param site              the site where the activity is performed
     * @param tipology          the typology of the activity
     * @param description       the description of activity
     * @param interventionTime  the estimeted intervention time of the activity
     * @param interruptible     the possibility of being interrupted
     * @param week              the week the activity is scheduled for
     * @param procedure         the associated procedure
     */
    public Activity(int id, Site site, Typology tipology, String description, 
                    int interventionTime, boolean interruptible, int week, 
                    Procedure procedure) {
        this(id, site, tipology, description, interventionTime, interruptible,
                week, procedure,"");
    }
    
    /**
     * Returns the id associated with this activity.
     * 
     * @return the activity id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the site where this activity is performed.
     * 
     * @return the activity site
     */
    public Site getSite() {
        return site;
    }

    /**
     * Returns the typology of this activity.
     * 
     * @return the activity typology
     */
    public Typology getTipology() {
        return tipology;
    }

    /**
     * Returns the description of this activity.
     * 
     * @return the activity decription
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the estimeted intervention time of this activity.
     * 
     * @return the activity estimeted time
     */
    public int getInterventionTime() {
        return interventionTime;
    }
    
    /**
     * Returns <code>true</code> if this activity is interruptible,
     * <code>false</code> otherwise.
     * 
     * @return <code>true</code> if this activity is interruptible,
     *         <code>false</code> otherwise
     */
    public boolean isInterruptible() {
        return interruptible;
    }
    
    /**
     * Returns the set of materials associated with this activity.
     * 
     * @return the set of material associated with activity
     */
    public Set<String> getMaterials() {
        return materials;
    }
    
    /**
     * Returns the week the activity is scheduled for.
     * 
     * @return the activity week
     */
    public int getWeek() {
        return week;
    }

    /**
     * Returns the procedure associated with this activity.
     * 
     * @return the activity procedure
     */
    public Procedure getProcedure() {
        return procedure;
    }

    /**
     * Returns the workspace associated with this activity.
     * 
     * @return the activity workspaceNotes
     */
    public String getWorkspaceNotes() {
        return workspaceNotes;
    }
    
    /**
     * Add a material to this activity.
     * 
     * @param material the material to be added
     */
    public void addMaterials(String material){
        materials.add(material);
        
    }
    
    /**
     * Returns the category of this activity.
     * 
     * @return the category activity
     */
    public abstract ActivityCategory getCategory();
    
    
    
    
    
    
}
