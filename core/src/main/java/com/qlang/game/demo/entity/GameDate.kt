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
    var minute: Float = 0f
        set(value) {
            field = value % 60
            hour += (value / 60f).toInt()
        }

    private val date = Date()

    companion object {
        const val TIME_SCALE = 30 //游戏世界和真实时间比（1分钟比, 真实世界1分钟，游戏里面30分钟）
    }

    constructor()

    constructor(day: Int, hour: Int, minute: Int) {
        this.day = day
        this.hour = hour
        this.hour += minute / 60
        this.day += hour / 24
        this.hour = hour % 24
        this.minute = minute % 60f
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

        minute = (_minute + hour * 60 + day * 24 * 60 + year * 365 * 24 * 60f) * TIME_SCALE
    }

    /**
     * 增加时间
     * @param second 秒钟，真实世界时间
     */
    fun addTime(second: Long) {
        this.minute = this.minute + (second / 60f) * TIME_SCALE
    }

    /**
     * @return 真实世界时间
     */
    fun getTime(): Date {
        //把分钟换成毫秒
        return date.apply { time = getGameTime().toLong() / TIME_SCALE * 60_000 }
    }

    /**
     * @return 游戏世界时间, 单位（分钟）
     */
    fun getGameTime(): Float {
        return day * 24 * 60L + hour * 60L + minute
    }

    override fun toString(): String {
        return "GameDate(season=$season, day=$day, hour=$hour, minute=$minute)"
    }


    enum class Season {
        SPRING, SUMMER, AUTUMN, WINTER
    }
}