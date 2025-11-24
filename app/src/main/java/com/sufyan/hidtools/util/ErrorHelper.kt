package com.sufyan.hidtools.util

import java.lang.Exception

/**
 * A sealed class to represent the result of an operation that can either succeed or fail.
 * This is useful for wrapping HID calls that might throw IOExceptions.
 */
sealed class HidResult<out T> {
    data class Success<out T>(val data: T) : HidResult<T>()
    data class Error(val exception: Exception) : HidResult<Nothing>()
}