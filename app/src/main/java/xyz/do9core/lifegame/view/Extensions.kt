package xyz.do9core.lifegame.view

fun <T> MutableList<T>.scaleToSize(targetSize: Int, value: () -> T) {
    while (this.size < targetSize) {
        add(value())
    }
}
