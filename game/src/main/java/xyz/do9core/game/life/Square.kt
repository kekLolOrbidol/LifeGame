package xyz.do9core.game.life

import xyz.do9core.game.Position

class Square(x: Int, y: Int) : TopLeft(x, y) {

    override fun positions(): Set<Position> {
        val (x, y) = topLeft
        return setOf(
            Position(x, y),
            Position(x + 1, y),
            Position(x + 2, y),
            Position(x, y + 1),
            Position(x, y + 2),
            Position(x + 2, y + 1),
            Position(x + 2, y + 2),
            Position(x + 1, y + 2)
        )
    }
}

fun LifePool.square(x: Int, y: Int) = newLife { Square(x, y) }