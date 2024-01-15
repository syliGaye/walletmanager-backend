package org.sylvestre.projects.wallet.backend.application.facade

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.sylvestre.projects.wallet.backend.dao.domains.WmUserDomain
import org.sylvestre.projects.wallet.backend.model.entities.WmUser
import java.util.*

@Component
class AuthenticationFacade @Autowired constructor(
    private val userDomain: WmUserDomain,
): IAuthenticationFacade {

    override fun getAuthenticatedUser(): Optional<WmUser> {
        var optionalUser: Optional<WmUser> = Optional.empty()

        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication !is AnonymousAuthenticationToken) {
            optionalUser = userDomain.findUserByUsername(authentication.name)
        }

        return optionalUser
    }

    override fun getAuthenticatedUsername(): Optional<String> {
        var username: String? = null

        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication !is AnonymousAuthenticationToken) {
            username = authentication.name
        }
        return Optional.ofNullable(username)
    }

    override fun getAuthenticatedUsers(): String? {
        var username: String? = null

        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication !is AnonymousAuthenticationToken) {
            username = authentication.name
        }
        return username
    }
}