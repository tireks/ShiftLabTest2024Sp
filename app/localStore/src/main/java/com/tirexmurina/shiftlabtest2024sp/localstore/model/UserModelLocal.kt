package com.tirexmurina.shiftlabtest2024sp.localstore.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserModelLocal(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    var firstName: String,
    var lastName: String
)
