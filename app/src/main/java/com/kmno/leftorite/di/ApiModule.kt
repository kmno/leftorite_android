/*
 * Creator: Kamran Noorinejad on 5/19/20 1:11 PM
 * Last modified: 5/19/20 1:11 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.di

import com.kmno.leftorite.data.api.ApiClientProvider
import org.koin.dsl.module

/**
 * Created by Kamran Noorinejad on 5/19/2020 AD 13:11.
 * Edited by Kamran Noorinejad on 5/19/2020 AD 13:11.
 */

val apiModule = module {
    single { ApiClientProvider() }
}