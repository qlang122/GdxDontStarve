package com.qlang.game.demo.db

import com.qlang.game.demo.config.trycatch
import com.qlang.game.demo.ktx.execAsync

fun <T, R> T.dbExecute(block: T.(DatabaseManager) -> R?, rtn: ((R?) -> Unit)? = null): T {
    execAsync({
        synchronized(DatabaseManager.LOCK) { trycatch { block(DatabaseManager.getInstance()) } }
    }, { rtn?.invoke(it) })
    return this
}

fun <T, R> T.dbExecute(block: T.(DatabaseManager) -> R?): R? = synchronized(DatabaseManager.LOCK) {
    trycatch { block(DatabaseManager.getInstance()) }
}