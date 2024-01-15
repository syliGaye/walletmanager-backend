package org.sylvestre.projects.wallet.backend.application.config

import org.sylvestre.projects.wallet.backend.application.resolver.SessionTokenHandlerArgumentResolver
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.hibernate.proxy.HibernateProxy
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*


@Configuration
class WebMvcConfig : WebMvcConfigurer {

    @Bean
    fun deviceResolverHandlerInterceptor(): DeviceResolverHandlerInterceptor {
        return DeviceResolverHandlerInterceptor()
    }

    @Bean
    fun deviceHandlerMethodArgumentResolver(): DeviceHandlerMethodArgumentResolver {
        return DeviceHandlerMethodArgumentResolver()
    }

    @Bean
    fun sessionHandlerMethodArgumentResolver(): SessionTokenHandlerArgumentResolver {
        return SessionTokenHandlerArgumentResolver()
    }

    @Bean
    fun localeResolver(): LocaleResolver {
        val sessionLocaleResolver = SessionLocaleResolver()
        sessionLocaleResolver.setDefaultLocale(Locale.FRENCH)
        return sessionLocaleResolver

    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        val objectMapper = ObjectMapper()
        val module = SimpleModule()
        //module.addSerializer(HibernateProxy::class.java, HibernateProxySerializer())
        //module.addSerializer(CsCustomer::class.java, CsCustomerSerializer())
        objectMapper.registerModule(module)
        converters.add(MappingJackson2HttpMessageConverter(objectMapper))
    }

    @Bean
    fun localeChangeInterceptor(): LocaleChangeInterceptor {
        val localeChangeInterceptor = LocaleChangeInterceptor()
        localeChangeInterceptor.paramName = "lang"
        return localeChangeInterceptor
    }

    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasename("classpath:locale/messages")
        messageSource.setDefaultEncoding("UTF-8")
        //messageSource.setDefaultEncoding("ISO-8859-1")
        return messageSource
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(localeChangeInterceptor())
        registry.addInterceptor(deviceResolverHandlerInterceptor())

        //registry.addInterceptor(MappedInterceptor(arrayOf("/${RestControllerEndpoint.API_BASE_SECURED_URL}/**"), apiSecuredHandlerInterceptor))
        //registry.addInterceptor(MappedInterceptor(arrayOf("/${RestControllerEndpoint.API_BASE_URL}/**"), apiHandlerInterceptor))
        //registry.addInterceptor(MappedInterceptor(arrayOf("/app/**","/**"), authenticatedUserInterceptor))
        //registry.addInterceptor(MappedInterceptor(arrayOf("/**"), helperInterceptor))
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*")
    }


    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {

        registry.addResourceHandler(
            "/webjars/**",
            "/static/**",
        )
            .addResourceLocations(
                "classpath:/META-INF/resources/webjars/",
                "classpath:/static/",
            )

        //registry.addResourceHandler("/${storageService.uploadDirectory}/**")
          //  .addResourceLocations(storageService.uploadLocation)

    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(deviceHandlerMethodArgumentResolver())
        resolvers.add(sessionHandlerMethodArgumentResolver())
    }
}