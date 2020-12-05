/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author alexd
 * @param <T> The entity managed by the DAL
 */
public abstract class AbstractDAL<T> {
    
    private Connection conn;
    
    public abstract T convertToEntity(ResultSet rs) throws SQLException;
    
    private ResultSet execute(String query) throws SQLException {
        conn = DatabaseConnection.getConnection();
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);
        return rs;
    }
    
    public final T executeQuery(String query) throws SQLException {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        ResultSet rs = execute(query);
        T dbEntity = null;
        if (rs.next()) {
            dbEntity = convertToEntity(rs);
        }
        if (connectionWasClosed) {
            conn.close();
        }
        return dbEntity;
    }
    
    public final Set<T> executeSetQuery(String query) throws SQLException {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        ResultSet rs = execute(query);
        Set<T> dbEntities = new HashSet<>();
        T dbEntity;
        while (rs.next()) {
            dbEntity = convertToEntity(rs);
            dbEntities.add(dbEntity);
        }
        if (connectionWasClosed) {
            conn.close();
        }
        return dbEntities;
    }
    
}
