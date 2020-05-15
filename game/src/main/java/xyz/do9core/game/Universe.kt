package xyz.do9core.game

data class Universe(
    private val initialState: Generation,
    private var maxLife: Int = -1
) : Iterable<Generation> {

    private inner class UniverseStateIterator : Iterator<Generation> {
        var current = initialState
        override fun hasNext(): Boolean {
            return if (maxLife < 0) true else {
                --maxLife > 0
            }
        }
        override fun next() = current.also { current = current.evolve() }
    }

    override fun iterator(): Iterator<Generation> = UniverseStateIterator()
}