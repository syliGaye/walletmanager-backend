package org.sylvestre.projects.wallet.backend.dao.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.sylvestre.projects.wallet.backend.model.entities.WmRole
import java.util.Optional

/**
 *
 * @author sylvestre
 */
interface WmRoleRepository: JpaRepository<WmRole, Long> {
    /**
     *
     * @param b
     * @return
     */
    fun findAllByActive(b: Boolean): List<WmRole>

    /**
     *
     * @param s
     * @return
     */
    fun findByName(s: String): Optional<WmRole>
}