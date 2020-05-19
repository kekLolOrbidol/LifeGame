package xyz.do9core.lifegame.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import xyz.do9core.lifegame.R
import kotlin.math.min

class LifeGameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Data
    private var matrix = BooleanMatrix(1, 1)

    fun setMatrix(newMatrix: BooleanMatrix?) {
        val new = newMatrix ?: return
        check(new.columnCount >= 1 && new.rowCount >= 1)
        if (!(new sizeEquals matrix)) {
            calculatePxSize(new, width, height)
        }
        matrix = new
        calculatePixels()
        invalidate()
    }

    // View
    private val paint = Paint()
    private val pixels = mutableListOf<RectF>()
    private var pxSize = 0f

    var liveColor: Int = 0
        set(value) {
            field = value
            paint.color = value
            invalidate()
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.LifeGameView) {
            liveColor = getColor(R.styleable.LifeGameView_liveColor, Color.GREEN)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        calculatePxSize(matrix, w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) {
            return
        }
        val drawRect = { rect: RectF -> canvas.drawRect(rect, paint) }
        pixels.forEach(drawRect)
    }

    private fun calculatePxSize(matrix: BooleanMatrix, w: Int, h: Int) {
        val widthF = w.toFloat()
        val heightF = h.toFloat()
        pxSize = min(widthF / matrix.columnCount, heightF / matrix.rowCount)
    }

    private fun calculatePixels() {
        pixels.clear()
        var top: Float
        var left: Float
        for (x in matrix.columnIndices) {
            left = pxSize * x
            for (y in matrix.rowIndices) {
                if (matrix[x, y]) {
                    top = pxSize * y
                    pixels.add(RectF(left, top, left + pxSize, top + pxSize))
                }
            }
        }
    }
}
