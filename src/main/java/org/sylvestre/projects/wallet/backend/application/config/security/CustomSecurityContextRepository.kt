package org.sylvestre.projects.wallet.backend.application.config.security

import org.springframework.security.core.context.SecurityContext
import org.springframework.security.web.context.HttpRequestResponseHolder
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomSecurityContextRepository: SecurityContextRepository {
    private val CUSTOMER_CONTEXT_KEY = "CUSTOMER_CONTEXT_KEY"

    override fun loadContext(requestResponseHolder: HttpRequestResponseHolder?): SecurityContext? {
        var context: SecurityContext? = null
        val requestCache = HttpSessionRequestCache()
        val request = requestResponseHolder?.request
        val response = requestResponseHolder?.response

        val savedRequest = requestCache.getRequest(request, response)

        // Check if there is a saved request, and if so, clear it from the cache

        // Check if there is a saved request, and if so, clear it from the cache
        if (savedRequest != null) {
            requestCache.removeRequest(request, response)
        }

        // Get the security context from the session

        // Get the security context from the session
        val session = request?.getSession(false)
        if (session != null) {
            val contextObj = session.getAttribute(CUSTOMER_CONTEXT_KEY)
            if (contextObj is SecurityContext) context = contextObj as SecurityContext
        }

        return context
    }

    override fun saveContext(context: SecurityContext?, request: HttpServletRequest?, response: HttpServletResponse?) {
        val session = request!!.getSession(false)
        if (session != null && context != null) {
            session.setAttribute(CUSTOMER_CONTEXT_KEY, context)
        }
    }

    override fun containsContext(request: HttpServletRequest?): Boolean {
        val session = request?.getSession(false)
        return if (session != null) {
            session.getAttribute(CUSTOMER_CONTEXT_KEY) != null
        } else false
    }

    fun removeContext(request: HttpServletRequest?, response: HttpServletResponse?){
        if (containsContext(request)) {
            request!!.getSession(false)?.removeAttribute(CUSTOMER_CONTEXT_KEY)
        }
    }
}