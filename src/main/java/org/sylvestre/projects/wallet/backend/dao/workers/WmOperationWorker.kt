package org.sylvestre.projects.wallet.backend.dao.workers

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Component
import org.sylvestre.projects.wallet.backend.dao.domains.WmOperationDomain
import org.sylvestre.projects.wallet.backend.dao.repositories.WmOperationRepository
import org.sylvestre.projects.wallet.backend.model.entities.WmAccount
import org.sylvestre.projects.wallet.backend.model.entities.WmOperation
import org.sylvestre.projects.wallet.backend.utils.OperationResult
import org.sylvestre.projects.wallet.backend.utils.helper.DateHelper
import java.util.*

/**
 *
 * @author sylvestre
 */
@Component
class WmOperationWorker: WmOperationDomain {
    private val logger = LoggerFactory.getLogger(WmOperationWorker::class.java)
    @Autowired private lateinit var wmOperationRepository: WmOperationRepository

    /**
     * (non-Javadoc)
     * @see WmOperationDomain.findByCodeOperation(String)
     */
    override fun findOperation(codeOperation: String): Optional<WmOperation> {
        logger.info("exécution findOperation({})...", codeOperation)
        val startTime = System.currentTimeMillis()
        val opt = wmOperationRepository.findByCodeOperation(codeOperation)
        if (!opt.isPresent) logger.error("Aucune transaction trouvée avec pour code: $codeOperation")
        logger.info("exécution findOperation({}) terminée en {} ms", codeOperation,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return opt
    }

    /**
     * (non-Javadoc)
     * @see WmOperationDomain.findAllByCompte(WmAccount)
     */
    override fun findOperations(compte: WmAccount): List<WmOperation> {
        logger.info("exécution findOperations({})...", compte)
        val startTime = System.currentTimeMillis()
        val list = wmOperationRepository.findAllByCompte(compte)
        logger.info("Recherche des transactions du compte {}: {} trouvé(s)",
            compte.numeroCompte, Integer.valueOf(list.size)
        )
        logger.info("exécution findOperations({}) terminée en {} ms", compte,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return list
    }

    /**
     * (non-Javadoc)
     * @see WmOperationDomain.findAllByDateOperation(Date)
     */
    override fun findOperations(date: Date): List<WmOperation> {
        logger.info("exécution findOperations({})...", DateHelper().format(date))
        val startTime = System.currentTimeMillis()
        val list = wmOperationRepository.findAllByDateOperation(date)
        logger.info("Recherche des transactions de la date {}: {} trouvé(s)",
            DateHelper().format(date), Integer.valueOf(list.size)
        )
        logger.info("exécution findOperations({}) terminée en {} ms", DateHelper().format(date),
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return list
    }

    /**
     * (non-Javadoc)
     * @see WmOperationDomain.save(WmOperation)
     */
    override fun save(model: WmOperation): OperationResult<WmOperation> {
        logger.info("exécution save({})...", model)
        val startTime = System.currentTimeMillis()
        var data: WmOperation? = null
        var errors: MutableMap<String, String>? = null

        try {
            data = wmOperationRepository.save(model)
            logger.info("Sauvegarde ou mise à jour de la transaction {}: effectée avec succès", model.codeOperation)
        }
        catch (ex: DataIntegrityViolationException) {
            errors = mutableMapOf()
            errors["Operation"] = "Une transaction existe déjà avec pour code: ${model.codeOperation}"
            logger.error("Une transaction existe déjà avec pour code: ${model.codeOperation}", ex)
        } catch (e: Exception){
            errors = mutableMapOf()
            errors["Operation"] = "Erreur de création ou de mise à jour de la transaction"
            logger.error("Erreur de création ou de mise à jour de la transaction", e)
        }

        logger.info("exécution save({}) terminée en {} ms", model,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return OperationResult(data, errors)
    }

    /**
     * (non-Javadoc)
     * @see WmOperationDomain.findById(Long)
     */
    override fun findById(id: Long): Optional<WmOperation> {
        logger.info("exécution findById({})...", id)
        val startTime = System.currentTimeMillis()
        val opt = wmOperationRepository.findById(id)
        if (!opt.isPresent) logger.error("Aucune transaction trouvée avec pour identifiant: $id")
        logger.info("exécution findById({}) terminée en {} ms", id,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return opt
    }

    /**
     * (non-Javadoc)
     * @see WmOperationDomain.findAll()
     */
    override fun findAll(): MutableList<WmOperation> {
        logger.info("exécution findAll()...")
        val startTime = System.currentTimeMillis()
        val list = wmOperationRepository.findAll()
        logger.info("Recherche de toutes les transactions: {} trouvée(s)", Integer.valueOf(list.size))
        logger.info("exécution findAll() terminée en {} ms",
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return list
    }

    /**
     * (non-Javadoc)
     * @see WmOperationDomain.findAllActive()
     */
    override fun findAllActive(): MutableList<WmOperation> {
        logger.info("exécution findAllActive()...")
        val startTime = System.currentTimeMillis()
        val list = wmOperationRepository.findAllByActive(true) as MutableList<WmOperation>
        logger.info("Recherche de toutes les transactions actives: {} trouvée(s)", Integer.valueOf(list.size))
        logger.info("exécution findAllActive() terminée en {} ms",
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return list
    }

    /**
     * (non-Javadoc)
     * @see WmOperationDomain.deleteAll()
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
     * @see WmOperationDomain.deleteById(Long)
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
     * @see WmOperationDomain.delete(WmOperation)
     */
    override fun delete(model: WmOperation) {
        logger.info("exécution delete({})...", model)
        val startTime = System.currentTimeMillis()
        this.findOperation(model.codeOperation).ifPresent {
            it.active = false
            this.save(it)
        }
        logger.info("exécution delete({}) terminée en {} ms", model,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
    }

    /**
     * (non-Javadoc)
     * @see WmOperationDomain.eraseById(Long)
     */
    override fun eraseById(id: Long) {
        logger.info("exécution eraseById({})...", id)
        val startTime = System.currentTimeMillis()
        try {
            wmOperationRepository.deleteById(id)
            logger.info("Suppression de la transaction avec pour identifiant {}: effectée avec succès", id)
        } catch (ex: EmptyResultDataAccessException) {
            logger.error("Aucune transaction n'existe avec l'identifiant: {}", id, ex)
        } catch (ex: Exception) {
            logger.error("Erreur de suppression de la transaction", ex)
            ex.printStackTrace()
        }
        logger.info("exécution eraseById({}) terminée en {} ms", id,
            (System.currentTimeMillis() - startTime)
        )
    }

    /**
     * (non-Javadoc)
     * @see WmOperationDomain.erase(WmOperation)
     */
    override fun erase(model: WmOperation) {
        logger.info("exécution erase({})...", model)
        val startTime = System.currentTimeMillis()
        try {
            wmOperationRepository.delete(model)
            logger.info("Suppression de la transaction {}: effectée avec succès", model.codeOperation)
        } catch (ex: EmptyResultDataAccessException) {
            logger.error("Aucune transaction n'existe avec l'identifiant: {}", model.id, ex)
        } catch (ex: Exception) {
            logger.error("Erreur de suppression de la transaction", ex)
            ex.printStackTrace()
        }
        logger.info("exécution erase({}) terminée en {} ms", model,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
    }

    /**
     * (non-Javadoc)
     * @see WmOperationDomain.count()
     */
    override fun count(): Long {
        logger.info("exécution count()...")
        val startTime = System.currentTimeMillis()
        val nbre = wmOperationRepository.count()
        logger.info("Compte toutes les transactions: {}", nbre)
        logger.info("exécution count() terminée en {} ms",
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return nbre
    }
}