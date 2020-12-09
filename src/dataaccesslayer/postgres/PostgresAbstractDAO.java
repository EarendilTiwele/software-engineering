/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author alexd
 * @param <T> the entity managed by the DAO
 */
public abstract class PostgresAbstractDAO<T> {

    private Connection connection;

    /**
     * Returns the entity of generic type <code>T</code> builded on the current
     * row of the ResultSet <code>rs</code>.
     *
     * @param rs the ResultSet with which to build the entity
     * @return the entity of generic type <code>T</code> builded on the current
     * row of the ResultSet <code>rs</code>
     * @throws SQLException if a database access error occurs
     */
    abstract T convertToEntity(ResultSet rs) throws SQLException;

    /**
     * Creates connection with a Postgres Database and returns a
     * <code>Statement</code> object created with the connection.
     *
     * @return a <code>Statement</code> object created with the connection to a
     * Postgres Database
     * @throws SQLException if a database access error occurs
     */
    private Statement createStatement() throws SQLException {
        connection = PostgresDAOFactory.createConnection();
        Statement stm = connection.createStatement();
        return stm;
    }

    /**
     * Executes the given SQL query, which may be an <code>update</code> or
     * <code>delete</code> statement, and returns the count of the rows affected
     * by the SQL Data Manipulation Language (DML) statements.
     *
     * @param query an SQL Data Manipulation Language (DML) statement, such as
     * <code>UPDATE</code> or <code>DELETE</code>
     * @return the count of the rows affected by the SQL Data Manipulation
     * Language (DML) statements
     * @throws SQLException if a database access error occurs
     */
    public final int executeUpdate(String query) throws SQLException {
        boolean connectionWasClosed = PostgresDAOFactory.connectionIsClosed();
        Statement stm = createStatement();
        int affectedRows = stm.executeUpdate(query);
        if (connectionWasClosed) {
            connection.close();
        }
        return affectedRows;
    }

    /**
     * Executes the given SQL statement, that can be an <code>insert</code>
     * statement or a <code>select</code> statement for a single element in the
     * database, which returns a single generic <code>T</code> object; returns
     * null if the element of the single <code>select</code> can't be found.
     *
     * @param query an SQL statement to be sent to the database, it can be an
     * <code>insert</code> statement or a <code>select</code> statement for a
     * single element in the database
     * @return returns a single generic <code>T</code> object; returns null if
     * the element of the single <code>select</code> can't be found
     * @throws SQLException if a database access error occurs
     */
    public final T executeQuery(String query) throws SQLException {
        boolean connectionWasClosed = PostgresDAOFactory.connectionIsClosed();
        Statement stm = createStatement();
        ResultSet rs = stm.executeQuery(query);
        T dbEntity = null;
        if (rs.next()) {
            dbEntity = convertToEntity(rs);
        }
        if (connectionWasClosed) {
            connection.close();
        }
        return dbEntity;
    }

    /**
     * Executes the given SQL statement, that can be a <code>select</code>
     * statement, which returns a set of generic <code>T</code> objects; returns
     * null if the <code>select</code> statement can't find any element.
     *
     * @param query an SQL statement to be sent to the database, that can be a
     * <code>select</code> statement
     * @return returns a set of generic <code>T</code> objects; returns null if
     * the <code>select</code> statement can't find any element
     * @throws SQLException if a database access error occurs
     */
    public final Set<T> executeSetQuery(String query) throws SQLException {
        boolean connectionWasClosed = PostgresDAOFactory.connectionIsClosed();
        Statement stm = createStatement();
        ResultSet rs = stm.executeQuery(query);
        Set<T> dbEntities = new HashSet<>();
        T dbEntity;
        while (rs.next()) {
            dbEntity = convertToEntity(rs);
            dbEntities.add(dbEntity);
        }
        if (connectionWasClosed) {
            connection.close();
        }
        return dbEntities;
    }

}
