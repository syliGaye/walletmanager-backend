package org.sylvestre.projects.wallet.backend.dao.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.sylvestre.projects.wallet.backend.model.entities.WmUser
import java.util.Optional

/**
 *
 * @author sylvestre
 */
interface WmUserRepository: JpaRepository<WmUser, Long> {
    /**
     *
     * @param b
     * @return
     */
    fun findAllByActive(b: Boolean): List<WmUser>

    /**
     *
     * @param s
     * @return
     */
    fun findByUsername(s: String): Optional<WmUser>

    /**
     *
     * @param s
     * @return
     */
    fun findByEmail(s: String): Optional<WmUser>

    /**
     *
     * @param s
     * @return
     */
    fun findByApiKey(s: String): Optional<WmUser>
}