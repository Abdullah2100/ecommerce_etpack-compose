package com.example.core

import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

object Util {
    fun encryptionFactory(databaseName: String): SupportFactory {
        val passPhraseBytes = SQLiteDatabase.getBytes(databaseName.toCharArray())
        return SupportFactory(passPhraseBytes)
    }
}