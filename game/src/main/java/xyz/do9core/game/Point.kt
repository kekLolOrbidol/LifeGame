package xyz.do9core.game

inline class Point(val coordinate: Pair<Int, Int>) {

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
