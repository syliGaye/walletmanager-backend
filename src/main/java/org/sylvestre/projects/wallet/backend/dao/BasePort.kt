package org.sylvestre.projects.wallet.backend.dao

import org.sylvestre.projects.wallet.backend.utils.OperationResult
import java.util.Optional

/**
 * DAO (Data Access Object) de base de tous les objets persistants.
 * @author gaye
 * @param <T> Objet persistant.
 * @param <A> Identifiant de l'objet persistant.
 */
interface BasePort<T, A> {
    /**
     * Rend persistant un nouvel objet.
     * <p>
     * Dans le cas des identifiants auto-incrémentés et après l'appel à cette méthode, l'objet aura automatiquement son identifiant affecté.
     * </p>
     * @param model Nouvel objet à rendre persistant (requis).
     */
    fun save(model: T) : OperationResult<T>

    /**
     * Recherche et retourne un objet persistant suivant son <u>identifiant technique</u>.
     * @param id Identifiant technique de l'objet persistant recherché (requis).
     * @return Objet persistant trouvé ou <code>NULL</code> s'il n'a pas été trouvé.
     */
    fun findById(id : A) : Optional<T>

    /**
     * Recherche et retourne <u>tous</u> les objets persistants gérés par la DAO.
     * <p>
     * Cette fonction est <u>à utiliser avec prudence</u>, pour des objets persistants dont on sait qu'ils ne seront pas nombreux et que leurs nombre n'augmentera
     * pas avec le temps.
     * </p>
     * @return Liste des objets persistants trouvés (jamais <code>NULL</code>).
     */
    fun findAll() : MutableList<T>

    /**
     * Recherche et retourne <u>tous</u> les objets persistants actifs, gérés par la DAO.
     * <p>
     * Cette fonction est <u>à utiliser avec prudence</u>, pour des objets persistants dont on sait qu'ils ne seront pas nombreux et que leurs nombre n'augmentera
     * pas avec le temps.
     * </p>
     * @return Liste des objets persistants trouvés (jamais <code>NULL</code>).
     */
    fun findAllActive() : MutableList<T>

    /**
     * Supprime tous les objets persistants.
     */
    fun deleteAll()

    /**
     * rend inactif un objet persistant à partir de identifiant.
     * @param id Identifiant de l'objet persistant (requis).
     */
    fun deleteById(id : A)

    /**
     * Rend inactif un objet persistent.
     * @param model Object persistent à rendre inactif (requis).
     */
    fun delete(model: T)

    /**
     * Supprime un objet persistant à partir de identifiant.
     * @param id Identifiant de l'objet persistant (requis).
     */
    fun eraseById(id: A)

    /**
     * Supprime un objet persistant.
     * @param model Objet persistant à supprimer (requis).
     */
    fun erase(model: T)

    /**
     * Compte et retourne le nombre de <u>tous</u> les objets persistants gérés par la DAO.
     * @return Nombre d'objets persistants trouvés.
     */
    fun count() : Long
}