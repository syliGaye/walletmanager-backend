package org.sylvestre.projects.wallet.backend.dao.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.sylvestre.projects.wallet.backend.model.entities.WmAccount
import org.sylvestre.projects.wallet.backend.model.entities.WmOperation
import java.util.Date
import java.util.Optional

/**
 *
 * @author sylvestre
 */
interface WmOperationRepository: JpaRepository<WmOperation, Long> {
    /**
     *
     *@param b
     * @return
     */
    fun findAllByActive(b: Boolean): List<WmOperation>

    /**
     *
     * @param s
     * @return
     */
    fun findByCodeOperation(s: String): Optional<WmOperation>

    /**
     *
     * @param compte
     * @return
     */
    fun findAllByCompte(compte: WmAccount): List<WmOperation>

    /**
     *
     * @param date
     * @return
     */
    fun findAllByDateOperation(date: Date): List<WmOperation>
}