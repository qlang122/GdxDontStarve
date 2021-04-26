package com.qlang.game.demo.db

import com.badlogic.gdx.sql.Database
import com.qlang.game.demo.data.WorlInfo

interface WorlDao {
    @JvmSuppressWildcards
    fun addWorls(worls: List<WorlInfo>): LongArray

    @JvmSuppressWildcards
    fun updateWorls(worls: List<WorlInfo>): Int

    fun delete(id: Int): Int

    fun deleteAll(): Int

    fun getWorl(id: Int): WorlInfo?

    @JvmSuppressWildcards
    fun getWorls(): List<WorlInfo>

    fun getCount(): Int

    @JvmSuppressWildcards
    fun getWorls(index: Int, size: Int): List<WorlInfo>
}

class WorlDaoImpl(val db: Database?) : WorlDao {
    override fun addWorls(worls: List<WorlInfo>): LongArray {
        val rtn = LongArray(worls.size)
        worls.forEachIndexed { index, info ->
            val sql = "INSERT OR REPLACE INTO `worl`(`id`,`role`,`name`,`days`,`time`,`createTime`) VALUES (nullif(?,0),?,?,?,?,?);"
            val values = HashMap<Int, Any?>().apply {
                put(0, info.id);put(1, info.role);put(2, info.name);put(3, info.days)
                put(4, info.time);put(5, info.createTime)
            }
            try {
                db?.executeInsert(sql, values)?.let { rtn[index] = it }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return rtn
    }

    override fun updateWorls(worls: List<WorlInfo>): Int {
        try {

        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return 0
    }

    override fun delete(id: Int): Int {
        try {
            val sql = "delete from `worl` where `id` = ?;"
            try {
                return db?.executeUpdateDelete(sql, hashMapOf<Int, Any?>(Pair(0, id))) ?: 0
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return 0
    }

    override fun deleteAll(): Int {
        try {
            val sql = "delete from `worl`;"
            return db?.executeUpdateDelete(sql, null) ?: 0
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {

        }
        return 0
    }

    override fun getWorl(id: Int): WorlInfo? {
        try {

        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {

        }
        return null
    }

    override fun getWorls(): List<WorlInfo> {
        try {

        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {

        }
        return emptyList()
    }

    override fun getWorls(index: Int, size: Int): List<WorlInfo> {
        try {

        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {

        }
        return emptyList()
    }

    override fun getCount(): Int {
        try {

        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {

        }
        return 0
    }

}