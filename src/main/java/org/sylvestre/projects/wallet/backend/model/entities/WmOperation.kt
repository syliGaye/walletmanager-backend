package org.sylvestre.projects.wallet.backend.model.entities

import org.sylvestre.projects.wallet.backend.model.common.BaseEntity
import org.sylvestre.projects.wallet.backend.utils.EncryptionUtility
import org.sylvestre.projects.wallet.backend.utils.Utility
import org.sylvestre.projects.wallet.backend.utils.helper.DateHelper
import java.io.Serializable
import java.util.Date
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
    name = "wm_operation",
    indexes = [Index(
        name = "i_wm_operation_code_operation",
        columnList = "code_operation",
        unique = true
    )]
)
data class WmOperation(
    /**
     * Le code de l'opération
     */
    @field:Column(name = "code_operation", nullable = false)
    var codeOperation: String = "",

    /**
     * Date de l'opération
     */
    @Column(name = "date_operation") var dateOperation: Date? = null,

    /**
     * Le sens de l'opération (Débit ou Crédit)
     */
    @Column(name = "sens") var sens: String = "",

    /**
     * La date à laquelle l'opération a eu effet sur le solde du compte.
     * <p>
     *     La date de débit ou de crédit sur le compte.
     * </p>
     */
    @Column(name = "date_effet") var dateEffet: Date? = null,

    /**
     * Le montant de la transaction
     */
    @Column(name = "montant") var montant: Double = 0.0,

    /**
     * Description de l'opération
     */
    @Column(name = "libelle_operation") var libelle: String = "",

    /**
     * Le compte sur lequel il y a eu l'opération
     */
    @ManyToOne(targetEntity = WmAccount::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "wm_account_id", nullable = false, foreignKey = ForeignKey(name = "fk_wm_operation_wm_account"))
    var compte: WmAccount? = null
): BaseEntity(), Serializable {
    override fun onPrePersist() {
        super.onPrePersist()
        this.active = true
    }
}