package xyz.do9core.game.life

import xyz.do9core.game.Point

data class Pixel(val position: Point) : Life() {

    constructor(x: Int, y: Int) : this(Point(x to y))

    override fun positions(): Set<Point> {
        return setOf(position)
    }
}

fun LifePool.point(x: Int, y: Int) = newLife { Pixel(x, y) }

fun LifePool.points(vararg positions: Pair<Int, Int>) {
    positions.forEach {
        newLife { Pixel(Point(it)) }
    }
}

fun LifePool.randPoints(
    topLeft: Pair<Int, Int>,
    bottomRight: Pair<Int, Int>,
    density: Double = 0.3
) {
    require(density in 0.0..1.0)
    val xRange = topLeft.first until bottomRight.first
    val yRange = topLeft.second until bottomRight.second
    val count = (xRange.count() * yRange.count() * density).toInt()
    val points = mutableSetOf<Point>()
    var p: Point
    repeat(count) {
        do {
            p = Point(xRange.random() to yRange.random())
        } while (p in points)
        newLife { Pixel(p) }
    }
}