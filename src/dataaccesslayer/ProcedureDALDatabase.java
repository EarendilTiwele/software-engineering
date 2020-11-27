/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Procedure;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author avall
 */
public class ProcedureDALDatabase implements ProcedureDAL {

    private Connection conn;

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
            if (connectionWasClosed) {
                conn.close();
            }
            return dbProcedure;
        } catch (SQLException ex) {
            Logger.getLogger(SiteDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
