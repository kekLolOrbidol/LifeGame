package xyz.do9core.game

data class Position(val x: Int, val y: Int) {

    fun neighbours(): Set<Position> {
        return setOf(
            Position(x - 1, y),
            Position(x, y - 1),
            Position(x + 1, y),
            Position(x, y + 1),
            Position(x - 1, y - 1),
            Position(x + 1, y - 1),
            Position(x - 1, y + 1),
            Position(x + 1, y + 1)
        )
    }
}