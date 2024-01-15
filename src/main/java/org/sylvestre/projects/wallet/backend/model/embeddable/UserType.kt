package org.sylvestre.projects.wallet.backend.model.embeddable

/**
 *
 * @author sylvestre
 */
object UserType {
    const val USER_ACTUATOR = "ACTUATOR"
    const val USER_DEFAULT = "USER"

    val USER_ROLES = listOf(USER_DEFAULT, USER_ACTUATOR)
}