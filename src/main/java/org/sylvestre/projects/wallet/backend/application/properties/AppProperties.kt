package org.sylvestre.projects.wallet.backend.application.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AppProperties {
    @Value("\${app.baseUrl}") // injection via application.properties
    var activationUrlPattern = ""

    @Value("\${app.fileDir}")
    var fileDirectory: String = ""
}