package xyz.do9core.game

import xyz.do9core.game.life.LifesPool

private const val INFINITY_TIME = "Inf."

data class Universe internal constructor(
    val width: Int,
    val height: Int,
    private val initialState: Generate,
    private val maxLife: Any = INFINITY_TIME
) : Iterable<Generate> {

    private inner class UniverseStateIterator : Iterator<Generate> {
        var current = initialState
        var life: Int? = maxLife as? Int

        override fun hasNext(): Boolean {
            val l = life ?: return true
            life = l - 1
            return l > 0
        }

        override fun next(): Generate {
            val temp = current
            current = current.evolve(width, height)
            return temp
        }
    }

    override fun iterator(): Iterator<Generate> = UniverseStateIterator()
}

fun createUniverse(
    width: Int,
    height: Int,
    time: Int? = null,
    lives: LifesPool.() -> Unit
): Universe {
    val points = LifesPool().apply(lives).points()
    val initGen = Generate(points)
    return Universe(width, height, initGen, time ?: INFINITY_TIME)
}
