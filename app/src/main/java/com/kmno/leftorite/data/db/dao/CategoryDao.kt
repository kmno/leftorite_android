/*
 * Creator: Kamran Noorinejad on 6/15/20 2:02 PM
 * Last modified: 6/15/20 2:02 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kmno.leftorite.core.Constants.dbName
import com.kmno.leftorite.data.model.Category

/**
 * Created by Kamran Noorinejad on 6/15/2020 AD 14:02.
 * Edited by Kamran Noorinejad on 6/15/2020 AD 14:02.
 */

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category)

    @Query("SELECT * from ${dbName}_categories WHERE is_active=1 ORDER BY is_new DESC")
    fun getCategories(): List<Category>

    @Query("SELECT * from ${dbName}_categories WHERE id=:catId")
    fun getCategoryById(catId: Int): Category

    @Query("DELETE FROM ${dbName}_categories")
    fun deleteAll()

}