package com.qlang.game.demo.entity

import java.util.*

class GameDate {
    var season: Season = Season.SPRING
    var day: Int = 0
        set(value) {
            field = value
            season = when (value / 91) {
                1 -> Season.SUMMER
                2 -> Season.AUTUMN
                3 -> Season.WINTER
                else -> Season.SPRING
            }
        }
    var hour: Int = 0
        set(value) {
            field = value % 24
            day += value / 24
        }
    var minute: Int = 0
        set(value) {
            field = value % 60
            hour += value / 60
        }

    companion object {
        const val TIME_SCALE = 30 //游戏世界和真实时间比（1分钟比）
    }

    constructor()

    constructor(day: Int, hour: Int, minute: Int) {
        this.day = day
        this.hour = hour
        this.hour += minute / 60
        this.day += hour / 24
        this.hour = hour % 24
        this.minute = minute % 60
    }

    constructor(date: Date) {
        setTime(date)
    }

    /**
     * 更新时间
     * @param date 真实世界时间
     */
    fun setTime(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val _minute = calendar.get(Calendar.MINUTE)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val day = calendar.get(Calendar.DAY_OF_YEAR)
        val year = calendar.get(Calendar.YEAR)

        minute = (_minute + hour * 60 + day * 24 * 60 + year * 365 * 24 * 60) / TIME_SCALE
    }

    /**
     * @return 游戏世界的时间
     */
    fun getTime(): Long {
        return day * 24 * 60L + hour * 60L + minute
    }

    enum class Season {
        SPRING, SUMMER, AUTUMN, WINTER
    }
}