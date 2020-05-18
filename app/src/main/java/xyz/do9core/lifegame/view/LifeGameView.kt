package xyz.do9core.lifegame.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
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
        check(new.width >= 1 && new.height >= 1)
        if (!(new sizeEquals matrix)) {
            calculatePxSize(new, width, height)
        }
        matrix = new
        calculatePixels()
        invalidate()
    }

    // View
    private val paint = Paint()
    private val pixels = mutableMapOf<RectF, Int>()
    private var pxSize = 0
    private var liveColor = Color.GREEN
    private var deadColor = Color.BLACK

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        calculatePxSize(matrix, w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) {
            return
        }
        for (entry in pixels) {
            paint.color = entry.value
            canvas.drawRect(entry.key, paint)
        }
    }

    private fun calculatePxSize(matrix: BooleanMatrix, w: Int, h: Int) {
        pxSize = min(w / matrix.width, h / matrix.height)
    }

    private fun calculatePixels() {
        pixels.clear()
        var top: Float
        var left: Float
        var pixel: RectF
        for (x in matrix.widthIndices) {
            left = pxSize * x.toFloat()
            for (y in matrix.heightIndices) {
                top = pxSize * y.toFloat()
                pixel = RectF(left, top, left + pxSize, top + pxSize)
                pixels[pixel] = if (matrix[x, y]) liveColor else deadColor
            }
        }
    }
}