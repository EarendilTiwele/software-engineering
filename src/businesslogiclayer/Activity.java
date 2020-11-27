/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;


import java.util.HashSet;
import java.util.Objects;
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
     * 
     * 
     */
    public Activity(int id, Site site, Typology tipology, String description, 
                    int interventionTime, boolean interruptible, int week, 
                    Procedure procedure, String workspaceNotes) {
        if(week<=0 || week>52){
            throw new IllegalArgumentException("week must be an integer between 1 and 52");
        }
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
     * Returns the hash code for this activity
     * The hash code is computed based on the all attribute of the activity.
     * 
     * @return a hash code value for this activity
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.id;
        hash = 79 * hash + Objects.hashCode(this.site);
        hash = 79 * hash + Objects.hashCode(this.tipology);
        hash = 79 * hash + Objects.hashCode(this.description);
        hash = 79 * hash + this.interventionTime;
        hash = 79 * hash + (this.interruptible ? 1 : 0);
        hash = 79 * hash + Objects.hashCode(this.materials);
        hash = 79 * hash + this.week;
        hash = 79 * hash + Objects.hashCode(this.procedure);
        hash = 79 * hash + Objects.hashCode(this.workspaceNotes);
        return hash;
    }

    
    /**
     * Compares this activity to the specified object.
     * The result is <code>true</code> if and only if the argument is not <code>null</code>
     * and is a <code>Activity</code> object that represents a activity with
     * the same name as this object.
     * 
     * @param obj the object to compare this <code>Activity</code> against
     * @return <code>true</code> if the given object represents a <code>Activity</code>
     *         equivalent to this activity, <code>false</code> otherwise 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Activity other = (Activity) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.interventionTime != other.interventionTime) {
            return false;
        }
        if (this.interruptible != other.interruptible) {
            return false;
        }
        if (this.week != other.week) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.workspaceNotes, other.workspaceNotes)) {
            return false;
        }
        if (!Objects.equals(this.site, other.site)) {
            return false;
        }
        if (!Objects.equals(this.tipology, other.tipology)) {
            return false;
        }
        if (!Objects.equals(this.materials, other.materials)) {
            return false;
        }
        if (!Objects.equals(this.procedure, other.procedure)) {
            return false;
        }
        if(!Objects.equals(this.getCategory(), other.getCategory())){
            return false;
        }
        return true;
    }

    /**
     * Returns the string rappresentation of all attribute of Activity object.
     * @return a string rappresentation of the activity.
     */
    @Override
    public String toString() {
        return "Activity:" +"Category= "+ this.getCategory()+ "id= " + id + 
                ", site= " + site + ", tipology= " + tipology + ", description= " 
                + description + ", interventionTime= " + interventionTime + 
                ", interruptible= " + interruptible + ", materials =" + 
                materials + ", week =" + week + ", procedure =" + procedure + 
                ", workspaceNotes=" + workspaceNotes;
    }

    
    
    
    
    /**
     * Returns the category of this activity.
     * 
     * @return the category activity
     */
    public abstract ActivityCategory getCategory();
    
    
    
    
    
    
}
