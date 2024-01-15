package org.sylvestre.projects.wallet.backend.model.entities

import org.sylvestre.projects.wallet.backend.model.common.BaseEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ForeignKey
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

/**
 *
 * @author sylvestre
 */
@Entity
@Table(
    name = "wm_account",
    indexes = [Index(
        name = "i_wm_account_numero_compte",
        columnList = "numero_compte",
        unique = true
    )]
)
data class WmAccount(
    /**
     * Le numéro de compte
     */
    @field:Column(name = "numero_compte", nullable = false)
    var numeroCompte: String = "",

    /**
     * Le solde du compte
     * <p>
     *     Le solde du compte lorsqu'on soustrait les transactions effectué à l'instant.
     *     Ce solde peut ne pas être égal au montant affiché.
     * </p>
     */
    @Column(name = "solde")
    var solde: Double = 0.0,

    /**
     * Le solde du compte affiché à l'instant
     */
    @Column(name = "montant_visible")
    var montantVisible: Double = 0.0,

    /**
     * Description du compte
     */
    @Column(name = "description")
    var description: String = "",

    /**
     * L'utilisateur a qui appartien le compte
     */
    @ManyToOne(targetEntity = WmUser::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "wm_user_id", nullable = false, foreignKey = ForeignKey(name = "fk_wm_account_wm_user"))
    var user: WmUser? = null
): BaseEntity(), Serializable {
    override fun onPrePersist() {
        super.onPrePersist()
        this.active = true
    }
}
