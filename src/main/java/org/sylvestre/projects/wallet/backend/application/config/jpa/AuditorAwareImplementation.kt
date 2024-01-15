package org.sylvestre.projects.wallet.backend.application.config.jpa

import org.springframework.context.annotation.Bean
import org.springframework.data.domain.AuditorAware
import java.util.*

/**
 * Default JPA Auditor provider
 *
 */
class AuditorAwareImplementation : AuditorAware<String> {

    @Bean
    override fun getCurrentAuditor(): Optional<String> = Optional.of("")
}