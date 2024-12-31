package com.tkr.vault.core.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

typealias BeforeTextChangedHandler = (s: String, start: Int, count: Int, after: Int) -> Unit
typealias OnTextChangedHandler = (s: String, start: Int, before: Int, count: Int) -> Unit
typealias AfterTextChangedHandler = (String) -> Unit

fun EditText.addBeforeTextChangedListener(onChangedTextListener: BeforeTextChangedHandler) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            onChangedTextListener(s.toString(), start, count, after)
        }
    })
}

fun EditText.addOnTextChangedListener(onChangedTextListener: OnTextChangedHandler) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onChangedTextListener(s.toString(), start, before, count)
        }
    })
}

fun EditText.addAfterTextChangedListener(onChangedTextListener: AfterTextChangedHandler) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            onChangedTextListener(s.toString())
        }
    })
}