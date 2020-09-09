package xyz.do9core.game.life

import xyz.do9core.game.Point

abstract class Life {
    abstract fun positions(): Set<Point>
}

abstract class TopLeftLife(val topLeft: Point) : Life() {
    constructor(x: Int, y: Int) : this(Point(x to y))
}

abstract class CenterLife(val center: Point) : Life() {
    constructor(x: Int, y: Int) : this(Point(x to y))
}
