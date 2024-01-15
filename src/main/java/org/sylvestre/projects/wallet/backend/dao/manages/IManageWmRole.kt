package org.sylvestre.projects.wallet.backend.dao.ports

import org.sylvestre.projects.wallet.backend.dao.BasePort
import org.sylvestre.projects.wallet.backend.model.entities.WmRole
import org.sylvestre.projects.wallet.backend.dao.repositories.WmRoleRepository
import java.util.Optional

/**
 *
 * @author sylvestre
 */
interface IManageWmRole: BasePort<WmRole, Long> {
    /**
     * (non-Javadoc)
     * @see WmRoleRepository.findByName(String)
     */
    fun findRole(name: String): Optional<WmRole>
}