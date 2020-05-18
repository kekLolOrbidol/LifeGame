package xyz.do9core.lifegame.view

class BooleanMatrix(
    val width: Int,
    val height: Int,
    defaultValue: Boolean = false
) {
    private val rows = Array(height) {
        BooleanArray(width) { defaultValue }
    }

    operator fun get(x: Int, y: Int): Boolean {
        require(x in 0 until width && y in 0 until height)
        return rows[y][x]
    }

    operator fun set(x: Int, y: Int, t: Boolean) {
        require(x in 0 until width && y in 0 until height)
        rows[y][x] = t
    }
}

infix fun BooleanMatrix.sizeEquals(booleanMatrix: BooleanMatrix): Boolean {
    return this.width == booleanMatrix.width && this.height == booleanMatrix.height
}

inline val BooleanMatrix.widthIndices get() = 0 until width
inline val BooleanMatrix.heightIndices get() = 0 until height
