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
        val rtn = LongArray(0)
        worls.forEach {
            val sql = "INSERT OR REPLACE INTO `worl`(`id`,`role`,`name`,`days`,`time`,`createTime`) " +
                    "VALUES (nullif(${it.id},0),'${it.role}','${it.name}',${it.days},${it.time},${it.createTime})"
            try {
                val query = db?.execSQL(sql)
            } catch (e: Throwable) {
                e.printStackTrace()
            } finally {

            }
        }
        return rtn
    }

    override fun updateWorls(worls: List<WorlInfo>): Int {
        try {

        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {

        }
        return 0
    }

    override fun delete(id: Int): Int {
        try {

        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {

        }
        return 0
    }

    override fun deleteAll(): Int {
        try {

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