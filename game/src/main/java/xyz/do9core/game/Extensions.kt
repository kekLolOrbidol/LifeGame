package xyz.do9core.game

import xyz.do9core.game.life.LifePool

@Suppress("NOTHING_TO_INLINE")
inline fun createUniverse(
    width: Int,
    height: Int,
    time: Int = Int.MIN_VALUE,
    noinline lives: LifePool.() -> Unit
): Universe = createUniverse(Size(width, height), time, lives)

fun createUniverse(
    size: Size,
    time: Int = Int.MIN_VALUE,
    lives: LifePool.() -> Unit
): Universe {
    val points = LifePool().apply(lives).getLives().flatMap { it.positions() }.toSet()
    val initGen = Generation(points, size)
    return Universe(initGen, time)
}