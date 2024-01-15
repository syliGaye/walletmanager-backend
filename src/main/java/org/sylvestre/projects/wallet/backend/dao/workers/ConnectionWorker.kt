package org.sylvestre.projects.wallet.backend.dao.workers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.sylvestre.projects.wallet.backend.dao.domains.ConnectionDomain
import org.sylvestre.projects.wallet.backend.dao.repositories.ConnectionEventRepository
import org.sylvestre.projects.wallet.backend.model.embeddable.ConnectionEvent
import org.sylvestre.projects.wallet.backend.utils.OperationResult

/**
 *
 */
@Service
class ConnectionWorker : ConnectionDomain {

    @Autowired
    private lateinit var connectionEventRepository: ConnectionEventRepository

    override fun save(model: ConnectionEvent): OperationResult<ConnectionEvent> {
        return OperationResult(connectionEventRepository.save(model))
    }

    override fun isFirstConnection(id: Long): Boolean = connectionEventRepository.countAllByUser_Id(id) == 0L

}