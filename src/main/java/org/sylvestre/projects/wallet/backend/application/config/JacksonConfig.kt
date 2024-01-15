package org.sylvestre.projects.wallet.backend.application.config


class JacksonConfig {
    /**
    @Bean
    fun objectMapper(): ObjectMapper? {
        val objectMapper = ObjectMapper()
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        val module = SimpleModule()
        module.addSerializer(CsCustomer::class.java, HibernateProxySerializer())
        objectMapper.registerModule(module)
        return objectMapper
    }
    **/
}