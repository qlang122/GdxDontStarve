package com.qlang.game.demo.tool

data class Point(var x: Int, var y: Int, var z: Float = 0f) {
    constructor(point: Point) : this(point.x, point.y, point.z) {
    }

    fun set(other: Point) {
        x = other.x;y = other.y;z = other.z
    }

    fun set(x: Int, y: Int) {
        this.x = x;this.y = y
    }

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false

        if (other is Point) return x == other.x && y == other.y && z == other.z

        return false
    }

    override fun toString(): String {
        return "Point(x=$x, y=$y, z=$z)"
    }
}
