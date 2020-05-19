package xyz.do9core.game

import xyz.do9core.game.life.LifePool

private const val INFINITY_TIME = "Inf."

data class Universe(
    val width: Int,
    val height: Int,
    private val initialState: Generation,
    private val maxLife: Any = INFINITY_TIME
) : Iterable<Generation> {

    private inner class UniverseStateIterator : Iterator<Generation> {
        var current = initialState
        var life: Int? = maxLife as? Int

        override fun hasNext(): Boolean {
            val l = life ?: return true
            life = l - 1
            return l > 0
        }

        override fun next(): Generation {
            val temp = current
            current = current.evolve(width, height)
            return temp
        }
    }

    override fun iterator(): Iterator<Generation> = UniverseStateIterator()
}

fun createUniverse(
    width: Int,
    height: Int,
    time: Int? = null,
    lives: LifePool.() -> Unit
): Universe {
    val points = LifePool().apply(lives).points()
    val initGen = Generation(points)
    return Universe(width, height, initGen, time ?: INFINITY_TIME)
}
