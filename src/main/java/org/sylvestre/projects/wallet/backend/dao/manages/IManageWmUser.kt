package org.sylvestre.projects.wallet.backend.dao.ports

import org.sylvestre.projects.wallet.backend.dao.BasePort
import org.sylvestre.projects.wallet.backend.model.entities.WmUser
import org.sylvestre.projects.wallet.backend.dao.repositories.WmUserRepository
import java.util.Optional

/**
 *
 * @author sylvestre
 */
interface IManageWmUser: BasePort<WmUser, Long> {
    /**
     * (non-Javadoc)
     * @see WmUserRepository.findByUsername(String)
     */
    fun findUserByUsername(username: String): Optional<WmUser>

    /**
     * (non-Javadoc)
     * @see WmUserRepository.findByEmail(String)
     */
    fun findUserByEmail(email: String): Optional<WmUser>

    /**
     * (non-Javadoc)
     * @see WmUserRepository.findByApiKey(String)
     */
    fun findUserByApikey(apikey: String): Optional<WmUser>
}