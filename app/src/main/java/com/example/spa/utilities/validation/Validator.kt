package com.example.spa.utilities.validation


import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Validator @Inject constructor() {

    var subject: String = ""
    var editText: EditText? = null
    var textInputLayout: TextInputLayout? = null
    var messageBuilders: ArrayList<MessageBuilder> = ArrayList()

    fun submit(editText: EditText): Validator {
        subject = editText.text.toString()
        this.editText = editText
        messageBuilders = ArrayList()
        return this
    }



    fun submit(editText: EditText, textInputLayout: TextInputLayout): Validator {
        subject = editText.text.toString()
        this.editText = editText
        this.textInputLayout = textInputLayout
        this.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!textInputLayout.error.isNullOrBlank()) {
                    textInputLayout.error = null
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
        messageBuilders = ArrayList()
        return this
    }

    fun checkEmpty(): MessageBuilder {
        return MessageBuilder(Type.EMPTY)
    }

    fun checkEmptyWithoutTrim(): MessageBuilder {
        return MessageBuilder(Type.EMPTY_WITHOUT_TRIM)
    }

    fun checkValidEmail(): MessageBuilder {
        return MessageBuilder(Type.EMAIL)
    }

    fun checkValidPhone(): MessageBuilder {
        return MessageBuilder(Type.PHONE)
    }

    fun checkMaxDigits(max: Int): MessageBuilder {
        return MessageBuilder(Type.MAX_LENGTH, max)
    }

    fun checkMinDigits(min: Int): MessageBuilder {
        return MessageBuilder(Type.MIN_LENGTH, min)
    }

    fun matchString(s: String): MessageBuilder {
        return MessageBuilder(Type.MATCH, s)
    }

    fun matchPatter(patter: String): MessageBuilder {
        return MessageBuilder(Type.PATTERN_MATCH, patter)
    }

    @Throws(ApplicationException::class)
    fun check(): Boolean {
        for (builder in messageBuilders) {
            try {
                when (builder.type) {
                    Type.EMPTY -> isEmpty(subject, builder.message, true)
                    Type.EMPTY_WITHOUT_TRIM -> isEmpty(subject, builder.message, false)
                    Type.PHONE -> isValidPhone(subject, builder.message)
                    Type.EMAIL -> isValidEmail(subject, builder.message)
                    Type.MAX_LENGTH -> checkMaxDigits(subject, builder.validLength, builder.message)
                    Type.MIN_LENGTH -> checkMinDigits(subject, builder.validLength, builder.message)
                    Type.MATCH -> match(subject, builder.match, builder.message)
                    Type.PATTERN_MATCH -> matchPatter(subject, builder.match, builder.message)
                }
                textInputLayout?.error = null
            } catch (e: ApplicationException) {
                editText?.requestFocus()
                textInputLayout?.isErrorEnabled = true
                textInputLayout?.error = e.localizedMessage
                e.type = ApplicationException.Type.VALIDATION
                throw e
            }
        }
        return true
    }

    @Throws(ApplicationException::class)
    fun isEmpty(subject: String?, errorMessage: String, byTrimming: Boolean) {
        var subject1: String? = subject ?: throw ApplicationException(errorMessage)
        if (byTrimming)
            subject1 = subject1!!.trim { it <= ' ' }
        if (subject1!!.isEmpty())
            throw ApplicationException(errorMessage)
    }

    @Throws(ApplicationException::class)
    fun isValidEmail(subject: String, errorMessage: String) {
        if (!subject.matches(Patterns.EMAIL_ADDRESS.pattern().toRegex()))
            throw ApplicationException(errorMessage)
    }

    @Throws(ApplicationException::class)
    fun isValidPhone(subject: String, errorMessage: String) {
        if (!subject.matches(Patterns.PHONE.pattern().toRegex()))
            throw ApplicationException(errorMessage)
    }

    @Throws(ApplicationException::class)
    fun checkMinDigits(subject: String, min: Int, errorMessage: String) {
        if (subject.length < min)
            throw ApplicationException(errorMessage)
    }

    @Throws(ApplicationException::class)
    fun checkMaxDigits(subject: String, max: Int, errorMessage: String) {
        if (subject.length > max)
            throw ApplicationException(errorMessage)
    }

    @Throws(ApplicationException::class)
    fun match(subject: String, toMatch: String?, errorMessage: String) {
        if (toMatch == null || subject != toMatch)
            throw ApplicationException(errorMessage)
    }

    @Throws(ApplicationException::class)
    fun matchPatter(subject: String?, pattern: String, errorMessage: String) {
        if (subject == null || !subject.matches(pattern.toRegex()))
            throw ApplicationException(errorMessage)
    }

    inner class MessageBuilder {

        var type: Type
        var validLength: Int = 0
        var message: String = ""
        var match: String = ""

        constructor(type: Type) {
            this.type = type
        }

        constructor(type: Type, validLength: Int) {
            this.type = type
            this.validLength = validLength
        }

        constructor(type: Type, match: String) {
            this.type = type
            this.match = match
        }

        fun errorMessage(message: String): Validator {
            this.message = message
            messageBuilders.add(this)
            return this@Validator
        }

        fun errorMessage(@StringRes message: Int): Validator {
            this.message = editText?.resources?.getString(message) ?: "Unknown Error"
            messageBuilders.add(this)
            return this@Validator
        }
    }

    enum class Type {
        EMPTY, EMAIL, PHONE, MIN_LENGTH, MAX_LENGTH, MATCH, PATTERN_MATCH, EMPTY_WITHOUT_TRIM
    }
}