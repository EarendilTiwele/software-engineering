/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

/**
 * @author Alfonso
 * @see Activity
 */
public class PlannedActivity extends Activity{
    
     
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
     */
    public PlannedActivity(int id, Site site, Typology tipology, String description,
            int interventionTime, boolean interruptible, int week,
            Procedure procedure, String workspaceNotes) {
        super(id, site, tipology, description, interventionTime, interruptible, 
                week, procedure, workspaceNotes);
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
    public PlannedActivity(int id, Site site, Typology tipology, String description, 
            int interventionTime, boolean interruptible, int week,
            Procedure procedure) {
        super(id, site, tipology, description, interventionTime, interruptible, 
                week, procedure);
        
    }
    
    
    /**
     * Returns the planned activity category.
     * @return the planned activity category
     */
    
    @Override
    public ActivityCategory getCategory(){
        return ActivityCategory.PLANNED;
    }
    
    
    
    
}
