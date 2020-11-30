/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import java.util.Objects;

/**
 * Activity assignment class for assigning an activity to a maintainer.
 * 
 * @author carbo
 */
public class Assignment {
    private Maintainer maintainer;
    private Activity activity;
    private String day;
    private int hour;

    /**
     * Construct an assignment specifying maintainer, activity, day and hour.
     * 
     * @param maintainer the maintainer of this assignment
     * @param activity   the activity of this assignment
     * @param day        the day of this assignment
     * @param hour       the hour of this assignment
     */
    public Assignment(Maintainer maintainer, Activity activity, String day, int hour) {
        this.maintainer = maintainer;
        this.activity = activity;
        this.day = day;
        this.hour = hour;
    }

    /**
     * Returns the maintainer of this assignment.
     * 
     * @return the maintainer of this assignment
     */
    public Maintainer getMaintainer() {
        return maintainer;
    }

    /**
     * Returns the activity of this assignment.
     * 
     * @return the activity of this assignment.
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * Returns the day of this assignment.
     * 
     * @return the day of this assignment.
     */
    public String getDay() {
        return day;
    }

    /**
     * Set the day of this assignment.
     * 
     * @param day the day of this assignment.
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * Returns the hour of this assignment.
     * 
     * @return the hour of this assignment.
     */
    public int getHour() {
        return hour;
    }

    /**
     * Set the hour of this assignment.
     * 
     * @param hour the hour of this assignment.
     */
    public void setHour(int hour) {
        this.hour = hour;
    }

    /**
     * Returns the hash code for this assignment.
     * The hash code is computed based on the maintainer, activity,
     * day and hour.
     * 
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.maintainer);
        hash = 67 * hash + Objects.hashCode(this.activity);
        hash = 67 * hash + Objects.hashCode(this.day);
        hash = 67 * hash + this.hour;
        return hash;
    }

    /**
     * Compares this assignment to the specified object.
     * The result is <code>true</code> if and only if the argument is not 
     * <code>null</code> and is a <code>Assignment</code> object that 
     * represents an assignment with the same maintainer, activity, day and
     * hour as this object.
     * 
     * @param obj the object to compare this <code>Assignment</code> against
     * @return <code>true</code> if the given object represents a
     *         <code>Assignment</code> equivalent to this assignment,
     *         <code>false</code> otherwise
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
        final Assignment other = (Assignment) obj;
        if (this.hour != other.hour) {
            return false;
        }
        if (!Objects.equals(this.day, other.day)) {
            return false;
        }
        if (!Objects.equals(this.maintainer, other.maintainer)) {
            return false;
        }
        if (!Objects.equals(this.activity, other.activity)) {
            return false;
        }
        return true;
    }
    
    
}
