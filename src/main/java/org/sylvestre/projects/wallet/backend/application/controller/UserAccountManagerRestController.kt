package org.sylvestre.projects.wallet.backend.application.restcontroller

import com.google.common.base.Preconditions
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolderStrategy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.sylvestre.projects.wallet.backend.application.config.security.CustomSecurityContextRepository
import org.sylvestre.projects.wallet.backend.application.controller.exception.BusinessResourceException
import org.sylvestre.projects.wallet.backend.application.controller.exception.GlobalHandlerControllerException
import org.sylvestre.projects.wallet.backend.application.facade.AuthenticationFacade
import org.sylvestre.projects.wallet.backend.dao.domains.WmRoleDomain
import org.sylvestre.projects.wallet.backend.dao.domains.WmUserDomain
import org.sylvestre.projects.wallet.backend.model.embeddable.UserType
import org.sylvestre.projects.wallet.backend.model.entities.WmRole
import org.sylvestre.projects.wallet.backend.model.entities.WmUser
import org.sylvestre.projects.wallet.backend.utils.EncryptionUtility
import org.sylvestre.projects.wallet.backend.utils.helper.DateHelper
import java.util.*
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(value = ["/account"])
class UserAccountManagerRestController(
    private val authenticationFacade: AuthenticationFacade,
    private val authenticationManager: AuthenticationManager,
    private val securityContextHolderStrategy: SecurityContextHolderStrategy,
    private val securityContextRepository: CustomSecurityContextRepository,
    private val userDomain: WmUserDomain,
    private val messageSource: MessageSource,
    private val exceptionHandler: GlobalHandlerControllerException,
    private val roleDomain: WmRoleDomain
) {
    /**
     * Logger
     */
    private val logger = LoggerFactory.getLogger(UserAccountManagerRestController::class.java)

    @GetMapping("/login")
    fun login(
        @RequestParam("username", defaultValue = "") username: String,
        @RequestParam("password", defaultValue = "") pass: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
        locale: Locale
    ): ResponseEntity<*>? {
        logger.info("exécution login({})...", username)
        val startTime = System.currentTimeMillis()
        val context = securityContextHolderStrategy.createEmptyContext()

        try {
            Preconditions.checkArgument((username != null && username != ""), "Le nom d'utilisateur est réquis")
            Preconditions.checkArgument((pass != null && pass != ""), "Le mot de passe est réquis")
            val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, pass))
            if (authentication != null){
                context.authentication = authentication
                securityContextHolderStrategy.context = context
                securityContextRepository.saveContext(context, request, response)
            }
        }
        catch (e: Exception){
            logger.error("exécution login", e)
            return exceptionHandler.businessResourceError(request, e as BusinessResourceException?)
        }
        logger.info(
            "exécution login({}) terminée en {} ms", username,
            (System.currentTimeMillis() - startTime)
        )
        return ResponseEntity<Any>(context, HttpStatus.FOUND)
    }

    @GetMapping("/logout")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<*>? {
        logger.info("exécution logout()...")
        val startTime = System.currentTimeMillis()
        var loggedOut = false
        try {
            authenticationFacade.getAuthenticatedUser().ifPresent {
                securityContextRepository.removeContext(request, response)
                SecurityContextHolder.clearContext()
                loggedOut = true
            }
        }
        catch (ex: Exception){
            logger.error("exécution login", ex)
            return exceptionHandler.businessResourceError(request, ex as BusinessResourceException?)
        }
        logger.info("exécution logout() terminée en {} ms", (System.currentTimeMillis() - startTime))
        return ResponseEntity<Any>(loggedOut, HttpStatus.OK)
    }

    @GetMapping("/signup")
    fun signup(
        @RequestParam("username", defaultValue = "") username: String,
        @RequestParam("email", defaultValue = "") email: String,
        @RequestParam("number", defaultValue = "") phone: String,
        @RequestParam("firstname", defaultValue = "") prenom: String,
        @RequestParam("lastname", defaultValue = "") nom: String,
        @RequestParam("password", defaultValue = "") password: String,
        @RequestParam("retypedpassword", defaultValue = "") retypedpassword: String,
        @RequestParam("birthdate", defaultValue = "") birthdate: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
        locale: Locale
    ): ResponseEntity<*>{
        logger.info("exécution signup()...")
        val startTime = System.currentTimeMillis()
        var signedUp = false
        try {
            Preconditions.checkArgument((username != null && username != ""), "Le nom d'utilisateur est réquis")
            Preconditions.checkArgument((password != null && password != ""), "Le mot de passe est réquis")
            Preconditions.checkArgument((email != null && email != ""), "L'adresse email est réquis")
            Preconditions.checkArgument((phone != null && phone != ""), "Le numéro de téléphone est réquis")
            if (password == retypedpassword){
                if (emailMatches(email)){
                    if (!userDomain.findUserByEmail(email).isPresent && !userDomain.findUserByUsername(username).isPresent){
                        val optRole = roleDomain.findRole(UserType.USER_DEFAULT)
                        if (optRole.isPresent){
                            val user = WmUser()
                            user.lastname = nom
                            user.firstname = prenom
                            user.email = email.toLowerCase()
                            user.username = username.toLowerCase()
                            user.password = password
                            user.birthDate = DateHelper().formatDateFromDB(birthdate)
                            user.phoneNumber = phone
                            user.roles.add(optRole.get())
                            if (userDomain.save(user).isSuccess) signedUp = true
                            else logger.error("Sauvegarde impossible!")
                        }
                        else logger.error("Ce rôle n'existe pas!")
                    }
                    else logger.error("Un utilisateur existe déjà!")
                }
                else logger.error("L'adresse email ne correspond pas!")
            }
            else logger.error("Le mot de passe n'est pas identique!")
        }
        catch (ex: Exception){
            logger.error("exécution signup", ex)
            return exceptionHandler.businessResourceError(request, ex as BusinessResourceException?)
        }
        logger.info("exécution signup() terminée en {} ms", (System.currentTimeMillis() - startTime))
        return ResponseEntity<Any>(signedUp, HttpStatus.CREATED)
    }

    @GetMapping("/active")
    fun active(
        @RequestParam("apikey", defaultValue = "") apikey: String,
        @RequestParam("key", defaultValue = "") key: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
        locale: Locale
    ): ResponseEntity<*>{
        logger.info("exécution active()...")
        val startTime = System.currentTimeMillis()
        var activated = false
        try {
            Preconditions.checkArgument((apikey != null && apikey != ""), "La clé d'utilisateur est réquis")
            userDomain.findUserByApikey(apikey).ifPresent {
                it.locked = false
                it.active = true
                it.enabled = true
                it.expired = false
                it.credentialsExpired = false
                if (userDomain.save(it).isSuccess) activated = true
            }
        }
        catch (ex: Exception){
            logger.error("exécution login", ex)
            return exceptionHandler.businessResourceError(request, ex as BusinessResourceException?)
        }
        logger.info("exécution active() terminée en {} ms", (System.currentTimeMillis() - startTime))
        return ResponseEntity<Any>(activated, HttpStatus.OK)
    }

    @GetMapping("/forgotpass")
    fun forgotpass(
        @RequestParam("email", defaultValue = "") email: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
        locale: Locale
    ): ResponseEntity<*>{
        logger.info("exécution forgotpass()...")
        val startTime = System.currentTimeMillis()
        var passchanged = false
        try {
            Preconditions.checkArgument((email != null && email != ""), "L'adresse email est réquis")
            userDomain.findUserByEmail(email).ifPresent {
                val pass = EncryptionUtility.randomizeMixCode_2(8)
                it.password = BCryptPasswordEncoder().encode(pass)
                it.locked = true
                it.active = false
                it.enabled = false
                if (userDomain.save(it).isSuccess) passchanged = true
            }
        }
        catch (ex: Exception){
            logger.error("exécution login", ex)
            return exceptionHandler.businessResourceError(request, ex as BusinessResourceException?)
        }
        logger.info("exécution forgotpass() terminée en {} ms", (System.currentTimeMillis() - startTime))
        return ResponseEntity<Any>(passchanged, HttpStatus.OK)
    }

    /**
     * @return
     */
    private fun emailMatches(emailAddress: String): Boolean {
        return Pattern.compile(REGEX_PATTERN)
            .matcher(emailAddress)
            .matches()
    }

    companion object{
        private const val REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"
    }
}