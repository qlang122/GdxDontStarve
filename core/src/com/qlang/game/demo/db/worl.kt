package com.qlang.game.demo.db

import com.badlogic.gdx.sql.Database
import com.qlang.game.demo.entity.WorlInfo

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
        val sql = "insert or replace into `worl`(`id`,`role`,`name`,`days`,`time`,`createTime`) values (nullif(?,0),?,?,?,?,?);"
        val rtn = LongArray(worls.size)
        worls.forEachIndexed { index, info ->
            val values = HashMap<Int, Any?>().apply {
                put(1, info.id);put(2, info.role);put(3, info.name);put(4, info.days)
                put(5, info.time);put(6, info.createTime)
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
        val sql = "update or abort `worl` set `id`=?,`role`=?,`name`=?,`days`=?,`time`=?,`createTime`=? where `id`=?;"
        var rtn = 0
        worls.forEach { info ->
            val values = HashMap<Int, Any?>().apply {
                put(1, info.id);put(2, info.role);put(3, info.name);put(4, info.days)
                put(5, info.time);put(6, info.createTime);put(7, info.id)
            }
            try {
                rtn += db?.executeUpdateDelete(sql, values) ?: 0
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return rtn
    }

    override fun delete(id: Int): Int {
        val sql = "delete from `worl` where `id`=?;"
        try {
            return db?.executeUpdateDelete(sql, hashMapOf<Int, Any?>(Pair(0, id))) ?: 0
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return 0
    }

    override fun deleteAll(): Int {
        val sql = "delete from `worl`;"
        try {
            return db?.executeUpdateDelete(sql, null) ?: 0
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return 0
    }

    override fun getWorl(id: Int): WorlInfo? {
        val sql = "select * from `worl` where `id`=?;"
        var result: WorlInfo? = null
        try {
            val cursor = db?.rawQuery(sql, arrayOf("$id"))
            try {
                if (cursor?.first() == true) {
                    cursor.run {
                        result = WorlInfo(getInt(0), getString(1), getString(2),
                                getInt(3), getLong(4), getLong(5))
                    }
                }
            } finally {
                cursor?.close()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return result
    }

    override fun getWorls(): List<WorlInfo> {
        val sql = "select * from `worl`;"
        val list = ArrayList<WorlInfo>()
        try {
            val cursor = db?.rawQuery(sql)
            try {
                while (cursor?.next() == true) {
                    cursor.run {
                        list.add(WorlInfo(getInt(0), getString(1), getString(2),
                                getInt(3), getLong(4), getLong(5)))
                    }
                }
            } finally {
                cursor?.close()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return list
    }

    override fun getWorls(index: Int, size: Int): List<WorlInfo> {
        val sql = "select * from `worl` limit ?,?;"
        val list = ArrayList<WorlInfo>()
        try {
            val cursor = db?.rawQuery(sql, arrayOf("$index", "$size"))
            try {
                while (cursor?.next() == true) {
                    cursor.run {
                        list.add(WorlInfo(getInt(0), getString(1), getString(2),
                                getInt(3), getLong(4), getLong(5)))
                    }
                }
            } finally {
                cursor?.close()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return list
    }

    override fun getCount(): Int {
        val sql = "select count(1) from `worl`;"
        var rtn = 0
        try {
            val cursor = db?.rawQuery(sql)
            try {
                if (cursor?.first() == true) {
                    rtn = cursor.getInt(0)
                }
            } finally {
                cursor?.close()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return rtn
    }

}