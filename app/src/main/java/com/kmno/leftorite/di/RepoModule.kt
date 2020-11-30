/*
 * Creator: Kamran Noorinejad on 11/30/20 12:27 PM
 * Last modified: 11/30/20 12:27 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.di

import com.kmno.leftorite.data.repository.*
import com.kmno.leftorite.data.repository.base.BaseRepository
import org.koin.dsl.module

/**
 * Created by Kamran Noorinejad on 11/30/2020 AD 12:27.
 * Edited by Kamran Noorinejad on 11/30/2020 AD 12:27.
 */

val repoModule = module {
    single { BaseRepository() }
    factory { ConfigRepository() }
    factory { AuthRepository() }
    factory { ItemsRepository() }
    factory { CategoryRepository() }
    factory { HistoryRepository() }
}