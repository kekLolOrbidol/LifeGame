package xyz.do9core.lifegame.view

class BooleanMatrix(
    val columnCount: Int,
    val rowCount: Int,
    defaultValue: Boolean = false
) {
    private val data = BooleanArray(columnCount * rowCount) { defaultValue }

    operator fun get(x: Int, y: Int): Boolean {
        require(x in 0 until columnCount && y in 0 until rowCount)
        return data[y * columnCount + x]
    }

    operator fun set(x: Int, y: Int, t: Boolean) {
        require(x in 0 until columnCount && y in 0 until rowCount)
        data[y * columnCount + x] = t
    }
}

infix fun BooleanMatrix.sizeEquals(another: BooleanMatrix): Boolean =
    this.columnCount == another.columnCount && this.rowCount == another.rowCount

inline val BooleanMatrix.columnIndices get() = 0 until columnCount
inline val BooleanMatrix.rowIndices get() = 0 until rowCount
