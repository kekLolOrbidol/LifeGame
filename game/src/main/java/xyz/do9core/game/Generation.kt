package xyz.do9core.game

data class Generation(val lives: Set<Position>) {

    internal fun evolve(size: Size): Generation {
        val next = mutableSetOf<Position>()
        for (x in 0 until size.width) {
            for (y in 0 until size.height) {
                val p = Position(x, y)
                val isLive = lives.contains(p)
                when (p.neighbours().intersect(lives).size) {
                    in 0..1 -> Unit
                    in 2..2 -> if (isLive) next.add(p)
                    in 3..3 -> next.add(p)
                    in 3..8 -> if (!isLive) next.add(p)
                }
            }
        }
        return Generation(next)
    }
}