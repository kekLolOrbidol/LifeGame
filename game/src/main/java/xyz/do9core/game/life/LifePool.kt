package xyz.do9core.game.life

class LifePool internal constructor() {

    private val lives = mutableListOf<Life>()

    fun newLife(life: () -> Life) {
        lives.add(life())
    }

    internal fun getLives(): List<Life> = lives.toList()
}