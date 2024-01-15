package org.sylvestre.projects.wallet.backend.application.bootstrap

import org.sylvestre.projects.wallet.backend.application.resolver.Constant
import org.sylvestre.projects.wallet.backend.dao.domains.WmRoleDomain
import org.sylvestre.projects.wallet.backend.model.embeddable.UserType
import org.sylvestre.projects.wallet.backend.model.entities.WmRole

class BootstrapCommon {
    fun loadDatabase(
        roleDomain: WmRoleDomain,
    ){
        if (roleDomain.count() == 0L) {
            UserType.USER_ROLES.forEach {
                roleDomain.save(WmRole(name = it.toUpperCase(), description = ""))
            }
        }
    }
}