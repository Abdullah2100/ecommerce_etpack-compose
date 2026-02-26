package com.example.core.database.Model

import androidx.room.PrimaryKey

abstract  class IsPassCondition {
    @PrimaryKey()
    var id: Int? = 0;
    var condition: Boolean=false;
}
