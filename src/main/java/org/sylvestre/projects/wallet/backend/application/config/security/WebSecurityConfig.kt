package org.sylvestre.projects.wallet.backend.application.config.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolderStrategy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val userDetailsServiceImplementation: UserDetailsServiceImplementation,
): WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers("/api/**").permitAll()
            .antMatchers("/rien").permitAll()
            .antMatchers("/account/login").permitAll()
            .antMatchers("/account/signup").permitAll()
            .antMatchers("/account/active").permitAll()
            .antMatchers("/account/forgotpass").permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic()
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web
            .ignoring()
            .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
                "/fonts/**", "/scss/**", "/email_templates/**", "/templates/**")
    }

    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService<UserDetailsService?>(userDetailsServiceImplementation)
            .passwordEncoder(BCryptPasswordEncoder())
    }

    @Bean
    @Throws(java.lang.Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun securityContextHolderStrategy(): SecurityContextHolderStrategy {
        return SecurityContextHolder.getContextHolderStrategy()
    }
}