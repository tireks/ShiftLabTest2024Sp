package com.tirexmurina.shiftlabtest2024sp.localstore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tirexmurina.shiftlabtest2024sp.localstore.model.UserModelLocal

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user : UserModelLocal)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): UserModelLocal

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getTableSize(): Int

    @Query("DELETE FROM users")
    suspend fun clearDatabase()

}