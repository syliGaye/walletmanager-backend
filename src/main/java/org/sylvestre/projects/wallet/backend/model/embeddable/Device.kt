package org.sylvestre.projects.wallet.backend.model.embeddable

import org.sylvestre.projects.wallet.backend.utils.EncryptionUtility
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.PrePersist

@Embeddable
data class Device(
    @field:Column(name = DEVICE_TYPE, nullable = false) val type: String,
    @field:Column(name = DEVICE_SDK, nullable = false) val sdk: String,
    @field:Column(name = DEVICE_VERSION, nullable = false) val version: String,
    @field:Column(name = DEVICE_BRAND, nullable = false) val brand: String,
    @field:Column(name = DEVICE_MANUFACTURED, nullable = false) val manufacturer: String,
    @field:Column(name = DEVICE_MODEL, nullable = false) val model: String
) {

    companion object {

        private const val DEVICE_HASH = "device_hash"

        private const val DEVICE_TYPE = "device_type"

        const val DEVICE_SDK = "device_sdk"

        const val DEVICE_VERSION = "device_version"

        const val DEVICE_BRAND = "device_brand"

        const val DEVICE_MANUFACTURED = "device_manufacturer"

        const val DEVICE_MODEL = "device_model"
    }

    @field:Column(name = DEVICE_HASH, nullable = false)
    lateinit var hash: String


    fun computeHash(): String {
        return Objects.requireNonNull<Optional<String>>(EncryptionUtility.sha256From(String.format("%s:%s:%s:%s:%s:%s", type, sdk, version, brand, manufacturer, model))).orElse("")
    }

    @PrePersist
    fun onPrePersist() {
        hash = computeHash()
    }
}
