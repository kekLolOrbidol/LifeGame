package xyz.do9core.lifegame.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import xyz.do9core.game.Position
import xyz.do9core.lifegame.R
import kotlin.math.min

class LifeGameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Data
    fun setLives(lives: Set<Position>?) {
        lives?.let {
            pixels.clear()
            var top: Float
            var left: Float
            for (cell in lives) {
                left = pxSize * cell.x
                top = pxSize * cell.y
                pixels.add(RectF(left, top, left + pxSize, top + pxSize))
            }
            invalidate()
        }
    }

    // View
    private val paint = Paint()
    private val pixels = mutableListOf<RectF>()
    private var pxSize = 0f

    var rows: Int = 0
        set(value) {
            field = value
            calculatePxSize(width, height)
            invalidate()
        }

    var columns: Int = 0
        set(value) {
            field = value
            calculatePxSize(width, height)
            invalidate()
        }

    var liveColor: Int = 0
        set(value) {
            field = value
            paint.color = value
            invalidate()
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.LifeGameView) {
            liveColor = getColor(R.styleable.LifeGameView_liveColor, Color.GREEN)
            rows = getInt(R.styleable.LifeGameView_rows, 1)
            columns = getInt(R.styleable.LifeGameView_columns, 1)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        calculatePxSize(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let { c ->
            pixels.forEach { pixel ->
                c.drawRect(pixel, paint)
            }
        }
    }

    private fun calculatePxSize(w: Int, h: Int) {
        val widthF = w.toFloat()
        val heightF = h.toFloat()
        pxSize = min(widthF / columns, heightF / rows)
    }
}
