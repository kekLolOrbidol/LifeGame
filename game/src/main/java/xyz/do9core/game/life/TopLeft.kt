package xyz.do9core.game.life

import xyz.do9core.game.Position

abstract class Life {
    abstract fun positions(): Set<Position>
}

abstract class TopLeft(val topLeft: Position): Life() {
    constructor(x: Int, y: Int): this(Position(x, y))
}

abstract class CenterLife(val center: Position): Life() {
    constructor(x: Int, y: Int): this(Position(x, y))
}