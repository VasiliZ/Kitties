package com.github.rtyvZ.kitties.common.exceptions

class NoContentExceptions(message: String) : Exception() {
    private var errorMessage = ""

    init {
        errorMessage = message
    }

    override fun toString(): String {
        return errorMessage
    }
}