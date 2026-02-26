package com.example.core.database.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("CurrentLocal")
data class CurrentLocal(
    @PrimaryKey()
    var id: Int? = 0,
    var name :String
)
