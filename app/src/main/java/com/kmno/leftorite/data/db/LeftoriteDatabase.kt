/*
 * Creator: Kamran Noorinejad on 6/15/20 2:07 PM
 * Last modified: 6/15/20 2:07 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kmno.leftorite.core.Constants.dbName
import com.kmno.leftorite.data.db.dao.CategoryDao
import com.kmno.leftorite.data.db.dao.ItemDao
import com.kmno.leftorite.data.model.Category

/**
 * Created by Kamran Noorinejad on 6/15/2020 AD 14:07.
 * Edited by Kamran Noorinejad on 6/15/2020 AD 14:07.
 */
@Database(entities = [Category::class], version = 1, exportSchema = false)
abstract class LeftoriteDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun itemDao(): ItemDao

    companion object {

        // @Volatile
        //private var INSTANCE: LeftoriteDatabase? = null

        private lateinit var INSTANCE: LeftoriteDatabase

        fun getDatabase(context: Context): LeftoriteDatabase? {
            // if (INSTANCE == null) {
            synchronized(LeftoriteDatabase::class.java) {
                // if (INSTANCE == null) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LeftoriteDatabase::class.java, dbName
                    ).build()
                }
            }
            //  }
            return INSTANCE
        }
    }

}