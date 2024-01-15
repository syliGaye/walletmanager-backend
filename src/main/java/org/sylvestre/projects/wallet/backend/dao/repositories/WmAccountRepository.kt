package org.sylvestre.projects.wallet.backend.dao.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.sylvestre.projects.wallet.backend.model.entities.WmAccount
import org.sylvestre.projects.wallet.backend.model.entities.WmUser
import java.util.Optional

/**
 *
 * @author sylvestre
 */
interface WmAccountRepository: JpaRepository<WmAccount, Long> {
    /**
     *
     * @param b
     * @return
     */
    fun findAllByActive(b: Boolean): List<WmAccount>

    /**
     *
     * @param s
     * @return
     */
    fun findByNumeroCompte(s: String): Optional<WmAccount>

    /**
     *
     * @param user
     * @return
     */
    fun findAllByUser(user: WmUser): List<WmAccount>
}