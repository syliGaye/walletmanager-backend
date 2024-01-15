package org.sylvestre.projects.wallet.backend.application.restcontroller

import com.google.common.base.Preconditions
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.sylvestre.projects.wallet.backend.application.controller.exception.BusinessResourceException
import org.sylvestre.projects.wallet.backend.application.controller.exception.GlobalHandlerControllerException
import org.sylvestre.projects.wallet.backend.dao.domains.WmAccountDomain
import org.sylvestre.projects.wallet.backend.dao.domains.WmOperationDomain
import org.sylvestre.projects.wallet.backend.dao.domains.WmUserDomain
import org.sylvestre.projects.wallet.backend.model.entities.WmAccount
import org.sylvestre.projects.wallet.backend.model.entities.WmOperation
import org.sylvestre.projects.wallet.backend.utils.EncryptionUtility
import org.sylvestre.projects.wallet.backend.utils.Utility
import org.sylvestre.projects.wallet.backend.utils.helper.DateHelper
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(value = ["/operation"])
class TransactionManagerRestController(
    private val wmOperationDomain: WmOperationDomain,
    private val wmAccountDomain: WmAccountDomain,
    private val wmUserDomain: WmUserDomain,
    private val exceptionHandler: GlobalHandlerControllerException
) {
    /**
     * Logger
     */
    private val logger = LoggerFactory.getLogger(TransactionManagerRestController::class.java)

    @GetMapping("/list")
    fun list(request: HttpServletRequest): ResponseEntity<*> {
        logger.info("exécution list()...")
        val startTime = System.currentTimeMillis()
        var list = listOf<WmOperation>()
        try {
            list = wmOperationDomain.findAllActive()
        }
        catch (ex: Exception){
            logger.error("exécution list", ex)
            return exceptionHandler.businessResourceError(request, ex as BusinessResourceException?)
        }
        logger.info("exécution list() terminée en {} ms", (System.currentTimeMillis() - startTime))
        return ResponseEntity<Any>(list, HttpStatus.FOUND)
    }

    @GetMapping("/account/list")
    fun list(
        @RequestParam("numerocompte", defaultValue = "") numCpte: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
        locale: Locale
    ): ResponseEntity<*> {
        logger.info("exécution list()...")
        val startTime = System.currentTimeMillis()
        var list = listOf<WmOperation>()
        try {
            Preconditions.checkArgument((numCpte != null && numCpte != ""), "Le numéro de compte est réquis")
            wmAccountDomain.findAccount(numCpte).ifPresent { list = wmOperationDomain.findOperations(it) }
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
    ): ResponseEntity<*> {
        logger.info("exécution get()...")
        val startTime = System.currentTimeMillis()
        var transactionFound = WmOperation()
        try {
            Preconditions.checkArgument((id != null && Utility.toLong(id) > 0L), "L'idantifiant de la transaction est réquis")
            wmOperationDomain.findById(Utility.toLong(id)).ifPresent { transactionFound = it }
        }
        catch (ex: Exception){
            logger.error("exécution get", ex)
            return exceptionHandler.businessResourceError(request, ex as BusinessResourceException?)
        }
        logger.info("exécution get() terminée en {} ms", (System.currentTimeMillis() - startTime))
        return ResponseEntity<Any>(transactionFound, HttpStatus.FOUND)
    }

    @PostMapping("/save")
    fun save(
        @RequestParam("numerocompte", defaultValue = "") numCpte: String,
        @RequestParam("sens", defaultValue = "") sens: String,
        @RequestParam("description", defaultValue = "") description: String,
        @RequestParam("dateoperation", defaultValue = "null") dateOperation: String,
        @RequestParam("montant", defaultValue = "0.0") montant: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
        locale: Locale
    ): ResponseEntity<*> {
        logger.info("exécution save()...")
        val startTime = System.currentTimeMillis()
        var transactionSaved: WmOperation? = null
        try {
            Preconditions.checkArgument((description != null && description != ""), "Le libellé de la transaction est réquis")
            Preconditions.checkArgument((sens != null && sens != ""), "Le sens de la transaction est réquis")
            Preconditions.checkArgument((numCpte != null && numCpte != ""), "Le numéro de compte est réquis")
            Preconditions.checkArgument((montant != null && Utility.toDouble(montant) > 0.0), "Le montant de la transaction est réquis")
            wmAccountDomain.findAccount(numCpte).ifPresent {
                val id = wmOperationDomain.count()
                val transaction = WmOperation()
                transaction.compte = it
                transaction.sens = sens.toUpperCase()
                transaction.dateOperation = Utility.toDate(dateOperation)
                transaction.montant = Utility.toDouble(montant)
                transaction.libelle = description
                transaction.codeOperation = getCodeOperation(Utility.toDate(dateOperation), id)
                val opTransaction = wmOperationDomain.save(transaction)
                if (opTransaction.isSuccess) transactionSaved = opTransaction.data
            }
        }
        catch (ex: Exception){
            logger.error("exécution save", ex)
            return exceptionHandler.businessResourceError(request, ex as BusinessResourceException?)
        }
        logger.info("exécution save() terminée en {} ms", (System.currentTimeMillis() - startTime))
        return ResponseEntity<Any>(transactionSaved, HttpStatus.CREATED)
    }

    /**
     * @return
     */
    fun getCodeOperation(dateOperation: Date, id: Long): String{
        return "TRANSAC${EncryptionUtility.randomizeStringCode(2)}${DateHelper(dateOperation).year}${DateHelper(dateOperation).monthOfYear}${DateHelper(dateOperation).dayOfMonth}$id"
    }
}