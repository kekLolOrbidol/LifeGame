package xyz.do9core.game

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