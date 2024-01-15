package org.sylvestre.projects.wallet.backend.model.embeddable

import org.sylvestre.projects.wallet.backend.model.entities.WmUser

/**
 *
 * @author sylvestre
 */
object UserProfiler {
    fun profile(user: WmUser) = UserProfile(user)

    class UserProfile(user: WmUser) {

        val id = user.id

        val username = user.username

        val firstname = user.firstname

        val lastname = user.lastname

        var email = user.email

        var phoneNumber = user.phoneNumber

        val fullname = "$firstname $lastname"

        val isEnabled = user.isEnabled

        var defaultUser: Boolean = false
        var actuator: Boolean = false

        var firstConnection = user.firstConnection

        init {
            val profile = user.roles
            defaultUser = profile.any { it.name == UserType.USER_DEFAULT }
            actuator = profile.any { it.name == UserType.USER_ACTUATOR }
        }
    }
}