package org.sylvestre.projects.wallet.backend.application.config

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

/**
 * Default Configuration for Async Support
 *
 */
@Configuration
@EnableAsync
class SpringAsyncConfig : AsyncConfigurer {

    override fun getAsyncExecutor(): Executor {
        val threadPoolTaskExecutor = ThreadPoolTaskExecutor()
        threadPoolTaskExecutor.corePoolSize = 2
        threadPoolTaskExecutor.maxPoolSize = 2
        threadPoolTaskExecutor.setQueueCapacity(500)
        threadPoolTaskExecutor.afterPropertiesSet()

        return threadPoolTaskExecutor
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? = null
}
