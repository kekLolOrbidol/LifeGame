package xyz.do9core.game.life

import xyz.do9core.game.Position

class LifePool internal constructor() {

    private val lives = mutableListOf<Life>()

    fun newLife(life: () -> Life) {
        lives.add(life())
    }

    internal fun points(): Set<Position> {
        return lives.flatMap { it.positions() }.toSet()
    }
}