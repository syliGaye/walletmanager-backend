package org.sylvestre.projects.wallet.backend.dao.ports

import org.sylvestre.projects.wallet.backend.dao.BasePort
import org.sylvestre.projects.wallet.backend.model.entities.WmAccount
import org.sylvestre.projects.wallet.backend.model.entities.WmOperation
import org.sylvestre.projects.wallet.backend.dao.repositories.WmOperationRepository
import java.util.Date
import java.util.Optional

/**
 *
 * @author sylvestre
 */
interface IManageWmOperation: BasePort<WmOperation, Long> {
    /**
     * (non-Javadoc)
     * @see WmOperationRepository.findByCodeOperation(String)
     */
    fun findOperation(codeOperation: String): Optional<WmOperation>

    /**
     * (non-Javadoc)
     * @see WmOperationRepository.findAllByCompte(WmAccount)
     */
    fun findOperations(compte: WmAccount): List<WmOperation>

    /**
     * (non-Javadoc)
     * @see WmOperationRepository.findAllByDateOperation(Date)
     */
    fun findOperations(date: Date): List<WmOperation>
}