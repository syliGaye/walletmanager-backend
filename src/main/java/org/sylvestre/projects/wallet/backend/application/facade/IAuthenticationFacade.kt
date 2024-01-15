package org.sylvestre.projects.wallet.backend.application.facade

import org.sylvestre.projects.wallet.backend.model.entities.WmUser
import java.util.*

interface IAuthenticationFacade {
    fun getAuthenticatedUser(): Optional<WmUser>

    fun getAuthenticatedUsername(): Optional<String>

    fun getAuthenticatedUsers(): String?
}