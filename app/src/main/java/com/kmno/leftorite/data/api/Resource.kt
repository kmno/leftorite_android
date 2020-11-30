/*
 * Creator: Kamran Noorinejad on 5/19/20 12:23 PM
 * Last modified: 5/19/20 12:23 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.api

/**
 * Created by Kamran Noorinejad on 5/19/2020 AD 12:23.
 * Edited by Kamran Noorinejad on 5/19/2020 AD 12:23.
 */
data class Resource<out T>(
    val state: State,
    val status: Boolean?,
    val message: String?,
    val data: T?
) {
    companion object {
        fun <T> success(status: Boolean?, message: String?, data: T?): Resource<T> {
            return Resource(State.SUCCESS, status, message, data)
        }

        fun <T> error(status: Boolean?, message: String?, data: T? = null): Resource<T> {
            return Resource(State.ERROR, status, message, data)
        }

        fun <T> loading(
            status: Boolean? = false,
            message: String? = null,
            data: T? = null
        ): Resource<T> {
            return Resource(State.LOADING, status, message, null)
        }

        fun loadingRepo(
            status: Boolean? = false,
            message: String? = null,
            data: Any? = null
        ): Resource<Any?> {
            return Resource(State.LOADING, status, message, null)
        }
    }
}

enum class State {
    SUCCESS,
    ERROR,
    LOADING
}