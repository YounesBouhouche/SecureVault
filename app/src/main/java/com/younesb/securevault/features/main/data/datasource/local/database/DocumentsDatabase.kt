package com.younesb.securevault.features.main.data.datasource.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.younesb.securevault.BuildConfig
import com.younesb.securevault.core.util.crypto.Crypto
import com.younesb.securevault.features.main.data.crypto.SqlCipherKeyManager
import com.younesb.securevault.features.main.data.datasource.local.database.dao.DocumentsDao
import com.younesb.securevault.features.main.data.datasource.local.database.models.Document
import com.younesb.securevault.features.main.data.datasource.local.database.models.DocumentTagCrossRef
import com.younesb.securevault.features.main.data.datasource.local.database.models.Folder
import com.younesb.securevault.features.main.data.datasource.local.database.models.Tag

@Database(
    entities = [
        Document::class,
        Folder::class,
        Tag::class,
        DocumentTagCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DocumentsDatabase : RoomDatabase() {
    abstract val dao: DocumentsDao

    companion object {
        const val NAME = "files_database"

        fun getInstance(context: Context): DocumentsDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                DocumentsDatabase::class.java,
                NAME
            )
                .openHelperFactory(
                    SqlCipherKeyManager(Crypto(BuildConfig.DB_KEY_ALIAS))
                        .getSupportFactory()
                )
                .build()
        }
    }
}