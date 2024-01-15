package org.sylvestre.projects.wallet.backend.application.restcontroller

import com.google.common.base.Preconditions
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.sylvestre.projects.wallet.backend.application.controller.exception.BusinessResourceException
import org.sylvestre.projects.wallet.backend.application.controller.exception.GlobalHandlerControllerException
import org.sylvestre.projects.wallet.backend.dao.domains.WmAccountDomain
import org.sylvestre.projects.wallet.backend.dao.domains.WmUserDomain
import org.sylvestre.projects.wallet.backend.model.entities.WmAccount
import org.sylvestre.projects.wallet.backend.utils.Utility
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(value = ["/billing"])
class CustomerAccountManagerRestController(
    private val wmAccountDomain: WmAccountDomain,
    private val wmUserDomain: WmUserDomain,
    private val exceptionHandler: GlobalHandlerControllerException
) {
    /**
     * Logger
     */
    private val logger = LoggerFactory.getLogger(CustomerAccountManagerRestController::class.java)

    @GetMapping("/list")
    fun list(request: HttpServletRequest): ResponseEntity<*>{
        logger.info("exécution list()...")
        val startTime = System.currentTimeMillis()
        var list = listOf<WmAccount>()
        try {
            list = wmAccountDomain.findAllActive()
        }
        catch (ex: Exception){
            logger.error("exécution list", ex)
            return exceptionHandler.businessResourceError(request, ex as BusinessResourceException?)
        }
        logger.info("exécution list() terminée en {} ms", (System.currentTimeMillis() - startTime))
        return ResponseEntity<Any>(list, HttpStatus.FOUND)
    }

    @GetMapping("/user/list")
    fun list(
        @RequestParam("username", defaultValue = "") username: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
        locale: Locale
    ): ResponseEntity<*>{
        logger.info("exécution list()...")
        val startTime = System.currentTimeMillis()
        var list = listOf<WmAccount>()
        try {
            Preconditions.checkArgument((username != null && username != ""), "Le nom d'utilisateur est réquis")
            wmUserDomain.findUserByUsername(username).ifPresent {
                list = wmAccountDomain.finAccounts(it)
            }
        }
        catch (ex: Exception){
            logger.error("exécution list", ex)
            return exceptionHandler.businessResourceError(request, ex as BusinessResourceException?)
        }
        logger.info("exécution list() terminée en {} ms", (System.currentTimeMillis() - startTime))
        return ResponseEntity<Any>(list, HttpStatus.FOUND)
    }

    @GetMapping("/get")
    fun get(
        @RequestParam("id", defaultValue = "-1") id: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
        locale: Locale
    ): ResponseEntity<*>{
        logger.info("exécution get()...")
        val startTime = System.currentTimeMillis()
        var accountFound = WmAccount()
        try {
            Preconditions.checkArgument((id != null && Utility.toLong(id) > 0L), "L'idantifiant du compte est réquis")
            wmAccountDomain.findById(Utility.toLong(id)).ifPresent { accountFound = it }
        }
        catch (ex: Exception){
            logger.error("exécution get", ex)
            return exceptionHandler.businessResourceError(request, ex as BusinessResourceException?)
        }
        logger.info("exécution get() terminée en {} ms", (System.currentTimeMillis() - startTime))
        return ResponseEntity<Any>(accountFound, HttpStatus.FOUND)
    }

    @PostMapping("/save")
    fun save(
        @RequestParam("username", defaultValue = "") username: String,
        @RequestParam("numerocompte", defaultValue = "") numCpte: String,
        @RequestParam("description", defaultValue = "") description: String,
        @RequestParam("solde", defaultValue = "0.0") solde: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
        locale: Locale
    ): ResponseEntity<*>{
        logger.info("exécution save()...")
        val startTime = System.currentTimeMillis()
        var accountSaved: WmAccount? = null
        try {
            Preconditions.checkArgument((username != null && username != ""), "Le nom d'utilisateur est réquis")
            Preconditions.checkArgument((numCpte != null && numCpte != ""), "Le numéro de compte est réquis")
            wmUserDomain.findUserByUsername(username).ifPresent {
                val compte = WmAccount()
                compte.user = it
                compte.numeroCompte = numCpte
                compte.description = description
                compte.solde = Utility.toDouble(solde)
                compte.montantVisible = Utility.toDouble(solde)
                val opCompte = wmAccountDomain.save(compte)
                if (opCompte.isSuccess) accountSaved = opCompte.data
            }
        }
        catch (ex: Exception){
            logger.error("exécution save", ex)
            return exceptionHandler.businessResourceError(request, ex as BusinessResourceException?)
        }
        logger.info("exécution save() terminée en {} ms", (System.currentTimeMillis() - startTime))
        return ResponseEntity<Any>(accountSaved, HttpStatus.CREATED)
    }
}