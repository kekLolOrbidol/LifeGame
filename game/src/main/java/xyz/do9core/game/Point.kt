package xyz.do9core.game

import java.io.Serializable

inline class Point(val coordinate: Pair<Int, Int>): Serializable {

    inline val x: Int get() = coordinate.first
    inline val y: Int get() = coordinate.second

    fun neighbours(): Set<Point> {
        return setOf(
            Point(x - 1 to y),
            Point(x to y - 1),
            Point(x + 1 to y),
            Point(x to y + 1),
            Point(x - 1 to y - 1),
            Point(x + 1 to y - 1),
            Point(x - 1 to y + 1),
            Point(x + 1 to y + 1)
        )
    }
}
