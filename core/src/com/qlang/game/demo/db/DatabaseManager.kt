package com.qlang.game.demo.db

import com.badlogic.gdx.sql.Database
import com.badlogic.gdx.sql.DatabaseFactory
import com.qlang.game.demo.ktx.trycatch

class DatabaseManager private constructor() {
    @Volatile
    private var _worlDao: WorlDao? = null

    var database: Database? = null
        private set

    private val SQL_CREATE_WORL = """CREATE TABLE IF NOT EXISTS `worl` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
`role` TEXT, `name` TEXT, `days` INTEGER, `time` TEXT, `createTime` TEXT)
    """.trimIndent()

    companion object {
        private val DB_NAME = "user.db"
        private val DB_VERSION = 1

        val LOCK = Any()

        private var INSTANCE: DatabaseManager? = null

        fun getInstance(): DatabaseManager = synchronized(DatabaseManager::class) {
            INSTANCE ?: DatabaseManager().also { INSTANCE = it }
        }
    }

    init {
        database = trycatch { DatabaseFactory.getNewDatabase(DB_NAME, DB_VERSION, null, null) }

        database?.trycatch {
            setupDatabase()
            openOrCreateDatabase()
            onOpen()
        }

    }

    fun release() {
        database?.closeDatabase()
        _worlDao = null
        database = null
        INSTANCE = null
    }

    private fun onOpen() {
        database?.execSQL(SQL_CREATE_WORL)
    }

    fun worlDao(): WorlDao {
        return _worlDao ?: synchronized(this) {
            _worlDao ?: WorlDaoImpl(database).also { _worlDao = it }
        }
    }
}