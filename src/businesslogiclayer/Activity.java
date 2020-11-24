/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import java.util.List;

/**
 *
 * @author avall
 */
public class Activity {

    private int id;
    private Site site;
    private String tipology;
    private String description;
    private int interventionTime;
    private boolean interruptible;
    private List<String> materials;
    private int week;
    private String workspaceNotes;
    private Procedure procedure;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getTipology() {
        return tipology;
    }

    public void setTipology(String tipology) {
        this.tipology = tipology;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInterventionTime() {
        return interventionTime;
    }

    public void setInterventionTime(int interventionTime) {
        this.interventionTime = interventionTime;
    }

    public boolean isInterruptible() {
        return interruptible;
    }

    public void setInterruptible(boolean interruptible) {
        this.interruptible = interruptible;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getWorkspaceNotes() {
        return workspaceNotes;
    }

    public void setWorkspaceNotes(String workspaceNotes) {
        this.workspaceNotes = workspaceNotes;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }
}
