package xyz.do9core.game.life

import xyz.do9core.game.Position

class TopLeftRing(x: Int, y: Int) : TopLeftLife(x, y) {

    override fun positions(): Set<Position> {
        val (x, y) = topLeft
        return setOf(
            Position(x + 1, y), Position(x + 2, y), Position(x + 3, y),
            Position(x, y + 1), Position(x, y + 2), Position(x, y + 3),
            Position(x + 1, y + 4), Position(x + 2, y + 4), Position(x + 3, y + 4),
            Position(x + 4, y + 1), Position(x + 4, y + 2), Position(x + 4, y + 3)
        )
    }
}

class CenterRing(x: Int, y: Int) : CenterLife(x, y) {

    override fun positions(): Set<Position> {
        val (x, y) = center
        return setOf(
            Position(x - 1, y - 2), Position(x, y - 2), Position(x + 1, y - 2),
            Position(x - 2, y - 1), Position(x - 2, y), Position(x - 2, y + 1),
            Position(x + 2, y - 1), Position(x + 2, y), Position(x + 2, y + 1),
            Position(x - 1, y + 2), Position(x, y + 2), Position(x + 1, y + 2)
        )
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun LifePool.ringLeftTop(x: Int, y: Int) = newLife { TopLeftRing(x, y) }

@Suppress("NOTHING_TO_INLINE")
inline fun LifePool.ringCenter(x: Int, y: Int) = newLife { CenterRing(x, y) }