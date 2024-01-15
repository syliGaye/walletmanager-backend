package org.sylvestre.projects.wallet.backend.model.embeddable

import org.sylvestre.projects.wallet.backend.model.common.BaseEntity
import org.sylvestre.projects.wallet.backend.model.entities.WmUser
import javax.persistence.*

/**
 * @author sylvestre
 */
@Entity
data class ConnectionEvent(
    @field:Column(nullable = false, updatable = false) val username: String,
    @field:Column(name = "user_agent", nullable = false, updatable = false) val userAgent: String,
    @field:ManyToOne(targetEntity = WmUser::class, fetch = FetchType.LAZY, optional = false)
    @field:JoinColumn(name = "owner", referencedColumnName = "id")
    val user: WmUser,
    @field:Embedded
    val device: Device
): BaseEntity()