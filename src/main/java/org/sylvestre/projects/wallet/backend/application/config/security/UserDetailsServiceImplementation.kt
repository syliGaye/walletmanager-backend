package org.sylvestre.projects.wallet.backend.application.config.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.sylvestre.projects.wallet.backend.dao.domains.WmUserDomain

@Service
class UserDetailsServiceImplementation @Autowired
constructor(private val authenticationManager: WmUserDomain) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails = authenticationManager.findUserByUsername(username).orElseThrow { UsernameNotFoundException("Invalid username and password") }

}
