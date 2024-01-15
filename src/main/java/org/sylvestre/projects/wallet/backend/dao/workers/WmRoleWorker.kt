package org.sylvestre.projects.wallet.backend.dao.workers

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Component
import org.sylvestre.projects.wallet.backend.dao.domains.WmRoleDomain
import org.sylvestre.projects.wallet.backend.dao.repositories.WmRoleRepository
import org.sylvestre.projects.wallet.backend.model.entities.WmRole
import org.sylvestre.projects.wallet.backend.utils.OperationResult
import java.util.*

/**
 *
 * @author sylvestre
 */
@Component
class WmRoleWorker: WmRoleDomain {
    private val logger = LoggerFactory.getLogger(WmRoleWorker::class.java)
    @Autowired private lateinit var wmRoleRepository: WmRoleRepository

    /**
     * (non-Javadoc)
     * @see WmRoleDomain.findRole(String)
     */
    override fun findRole(name: String): Optional<WmRole> {
        logger.info("exécution findRole({})...", name)
        val startTime = System.currentTimeMillis()
        val opt = wmRoleRepository.findByName(name)
        if (!opt.isPresent) logger.error("Aucun role trouvé avec pour nom: $name")
        logger.info("exécution findRole({}) terminée en {} ms", name,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return opt
    }

    /**
     * (non-Javadoc)
     * @see WmRoleDomain.save(WmRole)
     */
    override fun save(model: WmRole): OperationResult<WmRole> {
        logger.info("exécution save({})...", model)
        val startTime = System.currentTimeMillis()
        var data: WmRole? = null
        var errors: MutableMap<String, String>? = null

        try {
            data = wmRoleRepository.save(model)
            logger.info("Sauvegarde ou mise à jour du role {}: effectée avec succès", model.name)
        }
        catch (ex: DataIntegrityViolationException) {
            errors = mutableMapOf()
            errors["Role"] = "Un rôle existe déjà avec pour nom: ${model.name}"
            logger.error("Un rôle existe déjà avec pour nom: ${model.name}", ex)
        } catch (e: Exception){
            errors = mutableMapOf()
            errors["Role"] = "Erreur de création ou de mise à jour du rôle"
            logger.error("Erreur de création ou de mise à jour du rôle", e)
        }

        logger.info("exécution save({}) terminée en {} ms", model,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return OperationResult(data, errors)
    }

    /**
     * (non-Javadoc)
     * @see WmRoleDomain.findById(Long)
     */
    override fun findById(id: Long): Optional<WmRole> {
        logger.info("exécution findById({})...", id)
        val startTime = System.currentTimeMillis()
        val opt = wmRoleRepository.findById(id)
        if (!opt.isPresent) logger.error("Aucun rôle trouvé avec pour identifiant: $id")
        logger.info("exécution findById({}) terminée en {} ms", id,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return opt
    }

    /**
     * (non-Javadoc)
     * @see WmRoleDomain.findAll()
     */
    override fun findAll(): MutableList<WmRole> {
        logger.info("exécution findAll()...")
        val startTime = System.currentTimeMillis()
        val list = wmRoleRepository.findAll()
        logger.info("Recherche de tous les rôles: {} trouvé(s)", Integer.valueOf(list.size))
        logger.info("exécution findAll() terminée en {} ms",
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return list
    }

    /**
     * (non-Javadoc)
     * @see WmRoleDomain.findAllActive()
     */
    override fun findAllActive(): MutableList<WmRole> {
        logger.info("exécution findAllActive()...")
        val startTime = System.currentTimeMillis()
        val list = wmRoleRepository.findAllByActive(true) as MutableList<WmRole>
        logger.info("Recherche de tous les rôles actifs: {} trouvé(s)", Integer.valueOf(list.size))
        logger.info("exécution findAllActive() terminée en {} ms",
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return list
    }

    /**
     * (non-Javadoc)
     * @see WmRoleDomain.deleteAll()
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
     * @see WmRoleDomain.deleteById(Long)
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
     * @see WmRoleDomain.delete(WmRole)
     */
    override fun delete(model: WmRole) {
        logger.info("exécution delete({})...", model)
        val startTime = System.currentTimeMillis()
        this.findRole(model.name).ifPresent {
            it.active = false
            this.save(it)
        }
        logger.info("exécution delete({}) terminée en {} ms", model,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
    }

    /**
     * (non-Javadoc)
     * @see WmRoleDomain.eraseById(Long)
     */
    override fun eraseById(id: Long) {
        logger.info("exécution eraseById({})...", id)
        val startTime = System.currentTimeMillis()
        try {
            wmRoleRepository.deleteById(id)
            logger.info("Suppression du rôle avec pour identifiant {}: effectée avec succès", id)
        } catch (ex: EmptyResultDataAccessException) {
            logger.error("Aucun rôle n'existe avec l'identifiant: {}", id, ex)
        } catch (ex: Exception) {
            logger.error("Erreur de suppression du rôle", ex)
            ex.printStackTrace()
        }
        logger.info("exécution eraseById({}) terminée en {} ms", id,
            (System.currentTimeMillis() - startTime)
        )
    }

    /**
     * (non-Javadoc)
     * @see WmRoleDomain.erase(WmRole)
     */
    override fun erase(model: WmRole) {
        logger.info("exécution erase({})...", model)
        val startTime = System.currentTimeMillis()
        try {
            wmRoleRepository.delete(model)
            logger.info("Suppression du rôle {}: effectée avec succès", model.name)
        } catch (ex: EmptyResultDataAccessException) {
            logger.error("Aucun rôle n'existe avec l'identifiant: {}", model.id, ex)
        } catch (ex: Exception) {
            logger.error("Erreur de suppression du rôle", ex)
            ex.printStackTrace()
        }
        logger.info("exécution erase({}) terminée en {} ms", model,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
    }

    /**
     * (non-Javadoc)
     * @see WmRoleDomain.count()
     */
    override fun count(): Long {
        logger.info("exécution count()...")
        val startTime = System.currentTimeMillis()
        val nbre = wmRoleRepository.count()
        logger.info("Compte tous les rôles: {}", nbre)
        logger.info("exécution count() terminée en {} ms",
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return nbre
    }

}