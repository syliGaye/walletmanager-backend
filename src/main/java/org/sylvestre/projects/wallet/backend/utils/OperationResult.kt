package org.sylvestre.projects.wallet.backend.utils

class OperationResult<T>(val data: T?,
                         var errors: MutableMap<String, String>? = null) {

    val isSuccess: Boolean = errors == null || errors!!.isEmpty()
}