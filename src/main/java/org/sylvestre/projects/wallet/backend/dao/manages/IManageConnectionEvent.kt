package org.sylvestre.projects.wallet.backend.dao.ports

import org.sylvestre.projects.wallet.backend.model.embeddable.ConnectionEvent
import org.sylvestre.projects.wallet.backend.utils.OperationResult

/**
 * @author sylvestre
 */
interface IManageConnectionEvent {
    /**
     * @param model
     * @return
     */
    fun save(model: ConnectionEvent): OperationResult<ConnectionEvent>

    /**
     * @param id
     * @return
     */
    fun isFirstConnection(id: Long): Boolean
}