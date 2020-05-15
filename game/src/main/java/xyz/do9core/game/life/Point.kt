package xyz.do9core.game.life

import xyz.do9core.game.Position

data class Point(val position: Position) : Life() {

    constructor(x: Int, y: Int) : this(Position(x, y))

    override fun positions(): Set<Position> {
        return setOf(position)
    }
}

fun LifePool.point(x: Int, y: Int) = newLife { Point(x, y) }

fun LifePool.points(vararg positions: Pair<Int, Int>) {
    positions.forEach {
        newLife { Point(Position(it.first, it.second)) }
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
    val points = mutableSetOf<Position>()
    repeat(count) {
        var p: Position
        do {
            p = Position(xRange.random(), yRange.random())
        } while (p in points)
        newLife { Point(p) }
    }
}