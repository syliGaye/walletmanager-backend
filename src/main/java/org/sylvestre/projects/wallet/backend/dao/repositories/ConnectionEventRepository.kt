package org.sylvestre.projects.wallet.backend.dao.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.sylvestre.projects.wallet.backend.model.embeddable.ConnectionEvent

/**
 * @author sylvestre
 */
interface ConnectionEventRepository: JpaRepository<ConnectionEvent, Long> {
    /**
     * @param aLong
     * @return
     */
    fun countAllByUser_Id(aLong: Long): Long
}