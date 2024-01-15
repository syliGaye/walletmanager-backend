package org.sylvestre.projects.wallet.backend.dao.workers

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Component
import org.sylvestre.projects.wallet.backend.dao.domains.WmAccountDomain
import org.sylvestre.projects.wallet.backend.dao.repositories.WmAccountRepository
import org.sylvestre.projects.wallet.backend.model.entities.WmAccount
import org.sylvestre.projects.wallet.backend.model.entities.WmRole
import org.sylvestre.projects.wallet.backend.model.entities.WmUser
import org.sylvestre.projects.wallet.backend.utils.OperationResult
import java.util.*

/**
 *
 * @author sylvestre
 */
@Component
class WmAccountWorker: WmAccountDomain {
    private val logger = LoggerFactory.getLogger(WmAccountWorker::class.java)
    @Autowired private lateinit var wmAccountRepository: WmAccountRepository

    /**
     * (non-Javadoc)
     * @see WmAccountDomain.findAccount(String)
     */
    override fun findAccount(numeroCompte: String): Optional<WmAccount> {
        logger.info("exécution findAccount({})...", numeroCompte)
        val startTime = System.currentTimeMillis()
        val opt = wmAccountRepository.findByNumeroCompte(numeroCompte)
        if (!opt.isPresent) logger.error("Aucun compte trouvé avec pour numéro: $numeroCompte")
        logger.info("exécution findAccount({}) terminée en {} ms", numeroCompte,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return opt
    }

    /**
     * (non-Javadoc)
     * @see WmAccountDomain.finAccounts(WmUser)
     */
    override fun finAccounts(user: WmUser): List<WmAccount> {
        logger.info("exécution finAccounts({})...", user)
        val startTime = System.currentTimeMillis()
        val list = wmAccountRepository.findAllByUser(user)
        logger.info("Recherche des comptes de l'utilisateur {}: {} trouvé(s)",
            user.username, Integer.valueOf(list.size)
        )
        logger.info("exécution finAccounts({}) terminée en {} ms", user,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return list
    }

    /**
     * (non-Javadoc)
     * @see WmAccountDomain.save(WmAccount)
     */
    override fun save(model: WmAccount): OperationResult<WmAccount> {
        logger.info("exécution save({})...", model)
        val startTime = System.currentTimeMillis()
        var data: WmAccount? = null
        var errors: MutableMap<String, String>? = null

        try {
            data = wmAccountRepository.save(model)
            logger.info("Sauvegarde ou mise à jour du compte {}: effectée avec succès", model.numeroCompte)
        }
        catch (ex: DataIntegrityViolationException) {
            errors = mutableMapOf()
            errors["Compte"] = "Un compte existe déjà avec pour numéro: ${model.numeroCompte}"
            logger.error("Un compte existe déjà avec pour numéro: ${model.numeroCompte}", ex)
        } catch (e: Exception){
            errors = mutableMapOf()
            errors["Compte"] = "Erreur de création ou de mise à jour du compte"
            logger.error("Erreur de création ou de mise à jour du compte", e)
        }

        logger.info("exécution save({}) terminée en {} ms", model,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return OperationResult(data, errors)
    }

    /**
     * (non-Javadoc)
     * @see WmAccountDomain.findById(Long)
     */
    override fun findById(id: Long): Optional<WmAccount> {
        logger.info("exécution findById({})...", id)
        val startTime = System.currentTimeMillis()
        val opt = wmAccountRepository.findById(id)
        if (!opt.isPresent) logger.error("Aucun compte trouvé avec pour identifiant: $id")
        logger.info("exécution findById({}) terminée en {} ms", id,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return opt
    }

    /**
     * (non-Javadoc)
     * @see WmAccountDomain.findAll()
     */
    override fun findAll(): MutableList<WmAccount> {
        logger.info("exécution findAll()...")
        val startTime = System.currentTimeMillis()
        val list = wmAccountRepository.findAll()
        logger.info("Recherche de tous les comptes: {} trouvé(s)", Integer.valueOf(list.size))
        logger.info("exécution findAll() terminée en {} ms",
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return list
    }

    /**
     * (non-Javadoc)
     * @see WmAccountDomain.findAllActive()
     */
    override fun findAllActive(): MutableList<WmAccount> {
        logger.info("exécution findAllActive()...")
        val startTime = System.currentTimeMillis()
        val list = wmAccountRepository.findAllByActive(true) as MutableList<WmAccount>
        logger.info("Recherche de tous les comptes actifs: {} trouvé(s)", Integer.valueOf(list.size))
        logger.info("exécution findAllActive() terminée en {} ms",
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return list
    }

    /**
     * (non-Javadoc)
     * @see WmAccountDomain.deleteAll()
     */
    override fun deleteAll() {
        logger.info("exécution deleteAll()...")
        val startTime = System.currentTimeMillis()
        this.findAll()?.forEach {
            it.active = false
            this.save(it)
        }
        logger.info("exécution deleteAll() terminée en {} ms",
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
    }

    /**
     * (non-Javadoc)
     * @see WmAccountDomain.deleteById(Long)
     */
    override fun deleteById(id: Long) {
        logger.info("exécution deleteById({})...", id)
        val startTime = System.currentTimeMillis()
        this.findById(id).ifPresent {
            it.active = false
            this.save(it)
        }
        logger.info("exécution deleteById({}) terminée en {} ms", id,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
    }

    /**
     * (non-Javadoc)
     * @see WmAccountDomain.delete(WmAccount)
     */
    override fun delete(model: WmAccount) {
        logger.info("exécution delete({})...", model)
        val startTime = System.currentTimeMillis()
        this.findAccount(model.numeroCompte).ifPresent {
            it.active = false
            this.save(it)
        }
        logger.info("exécution delete({}) terminée en {} ms", model,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
    }

    /**
     * (non-Javadoc)
     * @see WmAccountDomain.eraseById(Long)
     */
    override fun eraseById(id: Long) {
        logger.info("exécution eraseById({})...", id)
        val startTime = System.currentTimeMillis()
        try {
            wmAccountRepository.deleteById(id)
            logger.info("Suppression du compte avec pour identifiant {}: effectée avec succès", id)
        } catch (ex: EmptyResultDataAccessException) {
            logger.error("Aucun compte n'existe avec l'identifiant: {}", id, ex)
        } catch (ex: Exception) {
            logger.error("Erreur de suppression du compte", ex)
            ex.printStackTrace()
        }
        logger.info("exécution eraseById({}) terminée en {} ms", id,
            (System.currentTimeMillis() - startTime)
        )
    }

    /**
     * (non-Javadoc)
     * @see WmAccountDomain.erase(WmAccount)
     */
    override fun erase(model: WmAccount) {
        logger.info("exécution erase({})...", model)
        val startTime = System.currentTimeMillis()
        try {
            wmAccountRepository.delete(model)
            logger.info("Suppression du compte {}: effectée avec succès", model.numeroCompte)
        } catch (ex: EmptyResultDataAccessException) {
            logger.error("Aucun compte n'existe avec l'identifiant: {}", model.id, ex)
        } catch (ex: Exception) {
            logger.error("Erreur de suppression du compte", ex)
            ex.printStackTrace()
        }
        logger.info("exécution erase({}) terminée en {} ms", model,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
    }

    /**
     * (non-Javadoc)
     * @see WmAccountDomain.count()
     */
    override fun count(): Long {
        logger.info("exécution count()...")
        val startTime = System.currentTimeMillis()
        val nbre = wmAccountRepository.count()
        logger.info("Compte tous les comptes: {}", nbre)
        logger.info("exécution count() terminée en {} ms",
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return nbre
    }
}