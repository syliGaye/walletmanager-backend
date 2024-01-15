package org.sylvestre.projects.wallet.backend.dao.workers

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Component
import org.sylvestre.projects.wallet.backend.dao.domains.WmUserDomain
import org.sylvestre.projects.wallet.backend.dao.repositories.WmUserRepository
import org.sylvestre.projects.wallet.backend.model.entities.WmRole
import org.sylvestre.projects.wallet.backend.model.entities.WmUser
import org.sylvestre.projects.wallet.backend.utils.OperationResult
import java.util.*

/**
 *
 * @author sylvestre
 */
@Component
class WmUserWorker: WmUserDomain {
    private val logger = LoggerFactory.getLogger(WmUserWorker::class.java)
    @Autowired private lateinit var wmUserRepository: WmUserRepository

    /**
     * (non-Javadoc)
     * @see WmUserDomain.findUserByUsername(String)
     */
    override fun findUserByUsername(username: String): Optional<WmUser> {
        logger.info("exécution findUserByUsername({})...", username)
        val startTime = System.currentTimeMillis()
        val opt = wmUserRepository.findByUsername(username)
        if (!opt.isPresent) logger.error("Aucun utilisateur trouvé avec pour nom d'utilisateur: $username")
        logger.info("exécution findUserByUsername({}) terminée en {} ms", username,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return opt
    }

    /**
     * (non-Javadoc)
     * @see WmUserDomain.findUserByEmail(String)
     */
    override fun findUserByEmail(email: String): Optional<WmUser> {
        logger.info("exécution findUserByEmail({})...", email)
        val startTime = System.currentTimeMillis()
        val opt = wmUserRepository.findByEmail(email)
        if (!opt.isPresent) logger.error("Aucun utilisateur trouvé avec pour email: $email")
        logger.info("exécution findUserByEmail({}) terminée en {} ms", email,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return opt
    }

    /**
     * (non-Javadoc)
     * @see WmUserDomain.findUserByApikey(String)
     */
    override fun findUserByApikey(apikey: String): Optional<WmUser> {
        logger.info("exécution findUserByApikey({})...", apikey)
        val startTime = System.currentTimeMillis()
        val opt = wmUserRepository.findByApiKey(apikey)
        if (!opt.isPresent) logger.error("Aucun utilisateur trouvé avec pour apikey: $apikey")
        logger.info("exécution findUserByApikey({}) terminée en {} ms", apikey,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return opt
    }

    /**
     * (non-Javadoc)
     * @see WmUserDomain.save(WmUser)
     */
    override fun save(model: WmUser): OperationResult<WmUser> {
        logger.info("exécution save({})...", model)
        val startTime = System.currentTimeMillis()
        var data: WmUser? = null
        var errors: MutableMap<String, String>? = null

        try {
            data = wmUserRepository.save(model)
            logger.info("Sauvegarde ou mise à jour de l'utilisateur {}: effectée avec succès", model.username)
        }
        catch (ex: DataIntegrityViolationException) {
            errors = mutableMapOf()
            errors["User"] = "Un utilisateur existe déjà avec pour nom d'utilisateur: ${model.username}"
            logger.error("Un Utilisateur existe déjà avec pour nom d'utilisateur: ${model.username}", ex)
        } catch (e: Exception){
            errors = mutableMapOf()
            errors["User"] = "Erreur de création ou de mise à jour de l'utilisateur"
            logger.error("Erreur de création ou de mise à jour de l'utilisateur", e)
        }

        logger.info("exécution save({}) terminée en {} ms", model,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return OperationResult(data, errors)
    }

    /**
     * (non-Javadoc)
     * @see WmUserDomain.findById(Long)
     */
    override fun findById(id: Long): Optional<WmUser> {
        logger.info("exécution findById({})...", id)
        val startTime = System.currentTimeMillis()
        val opt = wmUserRepository.findById(id)
        if (!opt.isPresent) logger.error("Aucun utilisateur trouvé avec pour identifiant: $id")
        logger.info("exécution findById({}) terminée en {} ms", id,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return opt
    }

    /**
     * (non-Javadoc)
     * @see WmUserDomain.findAll()
     */
    override fun findAll(): MutableList<WmUser> {
        logger.info("exécution findAll()...")
        val startTime = System.currentTimeMillis()
        val list = wmUserRepository.findAll()
        logger.info("Recherche de tous les utilisateurs: {} trouvé(s)", Integer.valueOf(list.size))
        logger.info("exécution findAll() terminée en {} ms",
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return list
    }

    /**
     * (non-Javadoc)
     * @see WmUserDomain.findAllActive()
     */
    override fun findAllActive(): MutableList<WmUser> {
        logger.info("exécution findAllActive()...")
        val startTime = System.currentTimeMillis()
        val list = wmUserRepository.findAllByActive(true) as MutableList<WmUser>
        logger.info("Recherche de tous les utilisateur actifs: {} trouvé(s)", Integer.valueOf(list.size))
        logger.info("exécution findAllActive() terminée en {} ms",
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return list
    }

    /**
     * (non-Javadoc)
     * @see WmUserDomain.deleteAll()
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
     * @see WmUserDomain.deleteById(Long)
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
     * @see WmUserDomain.delete(WmUser)
     */
    override fun delete(model: WmUser) {
        logger.info("exécution delete({})...", model)
        val startTime = System.currentTimeMillis()
        this.findUserByApikey(model.apiKey).ifPresent {
            it.active = false
            this.save(it)
        }
        logger.info("exécution delete({}) terminée en {} ms", model,
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
    }

    /**
     * (non-Javadoc)
     * @see WmUserDomain.eraseById(Long)
     */
    override fun eraseById(id: Long) {
        logger.info("exécution eraseById({})...", id)
        val startTime = System.currentTimeMillis()
        try {
            wmUserRepository.deleteById(id)
            logger.info("Suppression de l'utilisateur avec pour identifiant {}: effectée avec succès", id)
        } catch (ex: EmptyResultDataAccessException) {
            logger.error("Aucun utilisateur n'existe avec l'identifiant: {}", id, ex)
        } catch (ex: Exception) {
            logger.error("Erreur de suppression de l'utilisateur", ex)
            ex.printStackTrace()
        }
        logger.info("exécution eraseById({}) terminée en {} ms", id,
            (System.currentTimeMillis() - startTime)
        )
    }

    /**
     * (non-Javadoc)
     * @see WmUserDomain.erase(WmUser)
     */
    override fun erase(model: WmUser) {
        logger.info("exécution erase({})...", model)
        val startTime = System.currentTimeMillis()
        try {
            wmUserRepository.delete(model)
            logger.info("Suppression de l'utilisateur {}: effectée avec succès", model.username)
        } catch (ex: EmptyResultDataAccessException) {
            logger.error("Aucun utilisateur n'existe avec l'identifiant: {}", model.id, ex)
        } catch (ex: Exception) {
            logger.error("Erreur de suppression de l'utilisateur", ex)
            ex.printStackTrace()
        }
        logger.info("exécution erase({}) terminée en {} ms", model,
            (System.currentTimeMillis() - startTime)
        )
    }

    /**
     * (non-Javadoc)
     * @see WmUserDomain.count()
     */
    override fun count(): Long {
        logger.info("exécution count()...")
        val startTime = System.currentTimeMillis()
        val nbre = wmUserRepository.count()
        logger.info("Compte tous les utilisateurs: {}", nbre)
        logger.info("exécution count() terminée en {} ms",
            java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        )
        return nbre
    }
}