package org.sylvestre.projects.wallet.backend.model.entities

import org.sylvestre.projects.wallet.backend.model.common.BaseEntity
import org.sylvestre.projects.wallet.backend.model.common.Constant
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table

/**
 *
 * @author sylvestre
 */
@Entity
@Table(
    name = "wm_role",
    indexes = [Index(name = "i_wm_role_name", columnList = "name", unique = true)]
)
data class WmRole(
    @Column(nullable = false, length = Constant.JPA_CONSTRAINTS_MEDIUM)
    var name: String = "",
    var description: String = ""
): BaseEntity(), Serializable {
    override fun onPrePersist() {
        super.onPrePersist()
        this.active = true
    }
}
