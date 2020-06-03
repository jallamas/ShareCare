package org.jallamas.dam.sharecare.extended

import java.util.*

class ExtendedFunctions {

    companion object{
        fun <T> Optional<T>.unwrap(): T? = orElse(null)
    }

}