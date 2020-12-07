/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import datatransferobjects.Typology;
import dataaccesslayer.DAOFactory;
import dataaccesslayer.postgres.PostgresTypologyDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import dataaccesslayer.TypologyDAO;

/**
 *
 * @author alexd
 */
public class TypologyBO {
    
    private final TypologyDAO typologyDAO;

    public TypologyBO() {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        typologyDAO = postgresFactory.getTypologyDAO();
    }

    public Typology insert(Typology typology) throws SQLException {
        return typologyDAO.insert(typology);
    }

    public Typology update(Typology typology) throws SQLException {
        return typologyDAO.update(typology);
    }

    public Typology delete(int id) throws SQLException {
        return typologyDAO.delete(id);
    }

    public Typology get(int id) throws SQLException {
        return typologyDAO.get(id);
    }
    
    public List<Typology> getAll() throws SQLException {
        return new ArrayList<>(typologyDAO.getAll());
    }
    
}
