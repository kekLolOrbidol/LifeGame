package xyz.do9core.game

data class Generate(val lives: Set<Point>) {

    internal fun evolve(width: Int, height: Int): Generate {
        val next = mutableSetOf<Point>()
        for (x in 0 until width) {
            for (y in 0 until height) {
                val p = Point(x to y)
                val isLive = lives.contains(p)
                when (p.neighbours().intersect(lives).size) {
                    2 -> if (isLive) next.add(p)
                    3 -> next.add(p)
                    in 4..8 -> if (!isLive) next.add(p)
                }
            }
        }
        return Generate(next)
    }
}