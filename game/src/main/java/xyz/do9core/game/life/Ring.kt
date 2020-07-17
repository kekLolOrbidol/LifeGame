package xyz.do9core.game.life

import xyz.do9core.game.Point

class TopLeftRing(x: Int, y: Int) : TopLeftLife(x, y) {

    override fun positions(): Set<Point> {
        val (x, y) = topLeft.coordinate
        return setOf(
            Point(x + 1 to y), Point(x + 2 to y), Point(x + 3 to y),
            Point(x to y + 1), Point(x to y + 2), Point(x to y + 3),
            Point(x + 1 to y + 4), Point(x + 2 to y + 4), Point(x + 3 to y + 4),
            Point(x + 4 to y + 1), Point(x + 4 to y + 2), Point(x + 4 to y + 3)
        )
    }
}

class CenterRing(x: Int, y: Int) : CenterLife(x, y) {

    override fun positions(): Set<Point> {
        val (x, y) = center.coordinate
        return setOf(
            Point(x - 1 to y - 2), Point(x to y - 2), Point(x + 1 to y - 2),
            Point(x - 2 to y - 1), Point(x - 2 to y), Point(x - 2 to y + 1),
            Point(x + 2 to y - 1), Point(x + 2 to y), Point(x + 2 to y + 1),
            Point(x - 1 to y + 2), Point(x to y + 2), Point(x + 1 to y + 2)
        )
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun LifePool.ringLeftTop(x: Int, y: Int) = newLife { TopLeftRing(x, y) }

@Suppress("NOTHING_TO_INLINE")
inline fun LifePool.ringCenter(x: Int, y: Int) = newLife { CenterRing(x, y) }
