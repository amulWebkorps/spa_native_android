package com.example.mytips.utilities.validation

class ApplicationException constructor(message: String = "") : Throwable() {

    var type: Type? = null
    override var message: String = ""

    init {
        this.message = message
    }

    enum class Type {
        NO_INTERNET, NO_DATA, VALIDATION
    }
}