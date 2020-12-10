/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import datatransferobjects.Competency;
import datatransferobjects.Maintainer;
import java.util.Set;

/**
 *
 * @author alexd
 */
public interface MaintainerSkillsDAO {

    /**
     * Retrieves a <code>Set</code> of <code>Competency</code> objects
     * associated to a <code>maintainer</code> from a persistent storage.
     * Returns the <code>Set</code> of <code>Competency</code> objects
     * associated to the <code>maintainer</code> if the operation is successful;
     * <code>null</code> otherwise.
     *
     * @param maintainer the maintainer with the associated competencies
     * @return the <code>Set</code> of <code>Competency</code> objects
     * associated to the <code>maintainer</code> if the operation is successful;
     * <code>null</code> otherwise
     */
    public Set<Competency> getAllCompetencies(Maintainer maintainer);

}
