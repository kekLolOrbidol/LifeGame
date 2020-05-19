package xyz.do9core.game.life

import xyz.do9core.game.Point

class Square(x: Int, y: Int) : TopLeftLife(x, y) {

    override fun positions(): Set<Point> {
        val (x, y) = topLeft.coordinate
        return setOf(
            Point(x to y),
            Point(x + 1 to y),
            Point(x + 2 to y),
            Point(x to y + 1),
            Point(x to y + 2),
            Point(x + 2 to y + 1),
            Point(x + 2 to y + 2),
            Point(x + 1 to y + 2)
        )
    }
}

fun LifePool.square(x: Int, y: Int) = newLife { Square(x, y) }