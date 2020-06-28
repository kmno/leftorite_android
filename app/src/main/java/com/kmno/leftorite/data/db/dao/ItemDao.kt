/*
 * Creator: Kamran Noorinejad on 6/28/20 10:10 AM
 * Last modified: 6/28/20 10:10 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.db.dao

import androidx.room.Dao

/**
 * Created by Kamran Noorinejad on 6/28/2020 AD 10:10.
 * Edited by Kamran Noorinejad on 6/28/2020 AD 10:10.
 */
@Dao
interface ItemDao {
 /*   @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: Item)

    @Query("SELECT * from ${Constants.dbName}_items ORDER BY id ASC")
    fun getAllItems(): List<Item>

    @Query("SELECT * from ${Constants.dbName}_items WHERE category_id=:catId")
    fun getItemsByCategoryId(catId: Int): List<Item>

    @Query("SELECT * from ${Constants.dbName}_items WHERE id=:itemId")
    fun getItemsById(itemId: Int): Item

    @Query("DELETE FROM ${Constants.dbName}_items")
    fun deleteAll()*/

}