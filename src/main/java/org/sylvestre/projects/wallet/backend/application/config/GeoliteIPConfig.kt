package org.sylvestre.projects.wallet.backend.application.config

import com.maxmind.geoip2.DatabaseReader
import com.maxmind.geoip2.exception.GeoIp2Exception
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.io.IOException
import java.net.URLDecoder

@Configuration
class GeoliteIPConfig {

    @Bean
    @Throws(IOException::class, GeoIp2Exception::class)
    fun databaseReader(): DatabaseReader {
        val databaseUri = javaClass.classLoader.getResource("geolite2-country.mmdb")
        val resource = File(URLDecoder.decode(databaseUri?.file, "UTF-8"))
        return DatabaseReader.Builder(resource).build()
    }
}