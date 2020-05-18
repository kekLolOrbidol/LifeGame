package xyz.do9core.game

import xyz.do9core.game.life.LifePool

fun createUniverse(
    width: Int,
    height: Int,
    time: Int = Int.MIN_VALUE,
    lives: LifePool.() -> Unit
): Universe {
    val points = LifePool().apply(lives).getLives().flatMap { it.positions() }.toSet()
    val initGen = Generation(points)
    return Universe(width, height, initGen, time)
}
