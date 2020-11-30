/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Competency;
import businesslogiclayer.Procedure;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author avall
 */
public class ProcedureDALDatabase implements ProcedureDAL {

    private Connection conn;

    /**
     * Insert a specific procedure in the database 
     * @param procedure
     * @return the procedure inserted from the database
     */
    @Override
    public Procedure insert(Procedure procedure) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
                conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement
                    = conn.prepareStatement("insert into Procedure "
                            + "(name, smp)"
                            + "values (?,?) returning *;");
            prepareStatement.setString(1, procedure.getName());
            prepareStatement.setString(2, procedure.getSmp());
            ResultSet rs = prepareStatement.executeQuery();
            Procedure dbProcedure = null;
            while (rs.next()) {
                dbProcedure = new Procedure(rs.getInt("id"), rs.getString("name"), rs.getString("smp"));
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return dbProcedure;
        } catch (SQLException ex) {
            Logger.getLogger(SiteDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Update a specific procedure in the database 
     * @param procedure
     * @return the procedure updated from the database
     */
    @Override
    public Procedure update(Procedure procedure) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement
                    = conn.prepareStatement("update procedure "
                            + "set name = ?, smp = ? "
                            + "where id = ? returning *;");
            prepareStatement.setString(1, procedure.getName());
            prepareStatement.setString(2, procedure.getSmp());
            prepareStatement.setInt(3, procedure.getId());
            ResultSet rs = prepareStatement.executeQuery();
            Procedure dbProcedure = null;
            while (rs.next()) {
                dbProcedure = new Procedure(rs.getInt("id"), rs.getString("name"),
                        rs.getString("smp"));
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return dbProcedure;
        } catch (SQLException ex) {
            Logger.getLogger(SiteDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Delete a procedure with a specific id 
     * @param id
     * @return the procedure deleted from the database
     */
    @Override
    public Procedure delete(int id) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement
                    = conn.prepareStatement("delete from procedure "
                            + "where id = ? returning *;");
            prepareStatement.setInt(1, id);
            ResultSet rs = prepareStatement.executeQuery();
            Procedure dbProcedure = null;
            while (rs.next()) {
                dbProcedure = new Procedure(rs.getInt("id"), rs.getString("name"),
                        rs.getString("smp"));
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return dbProcedure;
        } catch (SQLException ex) {
            Logger.getLogger(SiteDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Get all procedures from the database
     * @return all procedures from the database
     */
    @Override
    public List<Procedure> getAll() {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        List<Procedure> procedureList = new ArrayList<>();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = 
                    conn.prepareStatement("select * from procedure;");
            ResultSet rs = prepareStatement.executeQuery();
            Procedure dbProcedure;
            while (rs.next()) {
                dbProcedure = new Procedure(rs.getInt("id"), rs.getString("name"),
                                  rs.getString("smp"));
                procedureList.add(dbProcedure);
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return procedureList;
        } catch (SQLException ex) {
            Logger.getLogger(SiteDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Get a specific procedure from the database
     * @param id
     * @return the procedure specified
     */
    @Override
    public Procedure get(int id) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement
                    = conn.prepareStatement("select * from Procedure "
                            + "where id = ?;");
            prepareStatement.setInt(1, id);
            ResultSet rs = prepareStatement.executeQuery();
            Procedure dbProcedure = null;
            while (rs.next()) {
                dbProcedure = new Procedure(rs.getInt("id"), rs.getString("name"),
                        rs.getString("smp"));
            }
            prepareStatement
                    = conn.prepareStatement("select tb1.id as ProcedureId, tb1.name as ProcedureName, tb1.smp as ProcedureSmp,\n" +
                    "competency.id as CompetencyId, competency.description as CompetencyDescription " +
                    "from (procedure inner join procedurehascompetencies on " +
                    "procedure.id = procedurehascompetencies.procedureid) as tb1 " +
                    "inner join competency on tb1.competencyid = competency.id "+
                    "where ProcedureId = ?; ");
            prepareStatement.setInt(1, id);
            rs = prepareStatement.executeQuery();
            Competency competency = null;
            while (rs.next()) {
                competency = new Competency(rs.getInt("CompetencyId"), rs.getString("CompetencyDescription"));
                dbProcedure.addCompetency(competency);
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return dbProcedure;
        } catch (SQLException ex) {
            Logger.getLogger(SiteDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Delete all procedures from the database
     * @return the procedures deleted from the database
     */
    @Override
    public List<Procedure> deleteAll()
    {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        List<Procedure> procedureList = new ArrayList<>();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = 
                    conn.prepareStatement("delete from procedure RETURNING *;");
            ResultSet rs = prepareStatement.executeQuery();
            Procedure dbProcedure;
            while (rs.next()) {
                dbProcedure = new Procedure(rs.getInt("id"), rs.getString("name"),
                                  rs.getString("smp"));
                procedureList.add(dbProcedure);
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return procedureList;
        } catch (SQLException ex) {
            Logger.getLogger(SiteDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
