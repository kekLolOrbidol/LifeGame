package xyz.do9core.game

import xyz.do9core.game.life.LifePool

data class Universe(
    val width: Int,
    val height: Int,
    private val initialState: Generation,
    private var maxLife: Int = -1
) : Iterable<Generation> {

    private val size = Size(width, height)

    private inner class UniverseStateIterator : Iterator<Generation> {
        var current = initialState
        override fun hasNext(): Boolean {
            return if (maxLife < 0) true else {
                --maxLife > 0
            }
        }
        override fun next() = current.also { current = current.evolve(size) }
    }

    override fun iterator(): Iterator<Generation> = UniverseStateIterator()
}

fun createUniverse(
    width: Int,
    height: Int,
    time: Int = Int.MIN_VALUE,
    lives: LifePool.() -> Unit
): Universe {
    val points = LifePool().apply(lives).points()
    val initGen = Generation(points)
    return Universe(width, height, initGen, time)
}
