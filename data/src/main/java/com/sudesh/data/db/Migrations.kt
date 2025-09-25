package com.sudesh.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS articles_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT,
                description TEXT,
                url TEXT,
                imageUrl TEXT,
                publishedAt TEXT
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO articles_new (id, title, description, url, imageUrl, publishedAt)
            SELECT id, title, description, url, imageUrl, publishedAt FROM articles
            """.trimIndent()
        )
        db.execSQL("DROP TABLE articles")
        db.execSQL("ALTER TABLE articles_new RENAME TO articles")
    }
}