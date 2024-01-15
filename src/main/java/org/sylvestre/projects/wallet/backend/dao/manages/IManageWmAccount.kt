package org.sylvestre.projects.wallet.backend.dao.ports

import org.sylvestre.projects.wallet.backend.dao.BasePort
import org.sylvestre.projects.wallet.backend.model.entities.WmAccount
import org.sylvestre.projects.wallet.backend.dao.repositories.WmAccountRepository
import org.sylvestre.projects.wallet.backend.model.entities.WmUser
import java.util.Optional

/**
 *
 * @author sylvestre
 */
interface IManageWmAccount: BasePort<WmAccount, Long> {
    /**
     * (non-Javadoc)
     * @see WmAccountRepository.findByNumeroCompte(String)
     */
    fun findAccount(numeroCompte: String): Optional<WmAccount>

    /**
     * (non-Javadoc)
     * @see WmAccountRepository.findAllByUser(WmUser)
     */
    fun finAccounts(user: WmUser): List<WmAccount>
}