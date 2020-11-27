/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Typology;
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
 * @author alexd
 */
public class TypologyDALDatabase implements TypologyDAL {
    
    private Connection conn;

    /**
     * Insert a typology in a database
     * @param typology the typology to insert
     * @return the inserted typology
     */
    @Override
    public Typology insert(Typology typology) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = 
                    conn.prepareStatement("insert into typology "
                    + "(typo)"
                    + "values (?) returning *;");
            prepareStatement.setString(1, typology.getName());
            ResultSet rs = prepareStatement.executeQuery();
            Typology dbTypology = null;
            while (rs.next()) {
                dbTypology = new Typology(rs.getInt("id"), rs.getString("typo"));
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return dbTypology;
        } catch (SQLException ex) {
            Logger.getLogger(TypologyDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Update a typology in a database
     * @param typology the typology to update
     * @return the updated typology
     */
    @Override
    public Typology update(Typology typology) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = 
                    conn.prepareStatement("update typology "
                    + "set typo = ? "
                    + "where id = ? returning *;");
            prepareStatement.setString(1, typology.getName());
            prepareStatement.setInt(2, typology.getId());
            ResultSet rs = prepareStatement.executeQuery();
            Typology dbTypology = null;
            while (rs.next()) {
                dbTypology = new Typology(rs.getInt("id"), rs.getString("typo"));
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return dbTypology;
        } catch (SQLException ex) {
            Logger.getLogger(TypologyDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Delete a typology with given id from a database
     * @param id the id which identifies the typology
     * @return the deleted typology
     */
    @Override
    public Typology delete(int id) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = 
                    conn.prepareStatement("delete from typology "
                    + "where id = ? returning *;");
            prepareStatement.setInt(1, id);
            ResultSet rs = prepareStatement.executeQuery();
            Typology dbTypology = null;
            while (rs.next()) {
                dbTypology = new Typology(rs.getInt("id"), rs.getString("typo"));
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return dbTypology;
        } catch (SQLException ex) {
            Logger.getLogger(TypologyDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Delete all typologies from a database
     * @return the number of deleted typologies
     */
    @Override
    public int deleteAll() {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = 
                    conn.prepareStatement("delete from typology "
                    + "where true;");
            int deletedRows = prepareStatement.executeUpdate();
            if (connectionWasClosed) {
                conn.close();
            }
            return deletedRows;
        } catch (SQLException ex) {
            Logger.getLogger(TypologyDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /** 
     * Retrieve a typology with given id from a database
     * @param id the id which identifies the typology
     * @return the typology retrieved
     */
    @Override
    public Typology get(int id) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = 
                    conn.prepareStatement("select * from typology "
                    + "where id = ?;");
            prepareStatement.setInt(1, id);
            ResultSet rs = prepareStatement.executeQuery();
            Typology dbTypology = null;
            while (rs.next()) {
                dbTypology = new Typology(rs.getInt("id"), rs.getString("typo"));
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return dbTypology;
        } catch (SQLException ex) {
            Logger.getLogger(TypologyDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Retrieve all the typologies from a database
     * @return the list of the typologies retrieved
     */
    @Override
    public List<Typology> getAll() {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        List<Typology> typologies = new ArrayList<>();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = 
                    conn.prepareStatement("select * from typology;");
            ResultSet rs = prepareStatement.executeQuery();
            Typology dbTypology;
            while (rs.next()) {
                dbTypology = new Typology(rs.getInt("id"), rs.getString("typo"));
                typologies.add(dbTypology);
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return typologies;
        } catch (SQLException ex) {
            Logger.getLogger(TypologyDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
