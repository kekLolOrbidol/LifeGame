package xyz.do9core.lifegame

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import xyz.do9core.game.*
import xyz.do9core.game.life.randPoints
import xyz.do9core.lifegame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val size = Size(51, 51)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initContainer(w = size.width, h = size.height)

        var job: Job? = null
        binding.runButton.setOnClickListener {
            if (job != null) {
                job!!.cancel()
                job = null
                binding.runButton.text = "Restart"
                return@setOnClickListener
            }
            job = lifecycleScope.launch(Dispatchers.Default) {
                universe().forEachIndexed { index, generation ->
                    withContext(Dispatchers.Main) {
                        binding.genText.text = index.toString()
                        generation.renderInto(binding.container)
                    }
                    delay(500)
                }
            }
            binding.runButton.text = "Stop"
        }
    }

    private fun universe(): Universe {
        return createUniverse(size) {
            randPoints(5 to 5, 40 to 40, density = 0.05)
        }
    }

    private fun initContainer(w: Int, h: Int, deadColor: Int = Color.BLACK) {
        binding.container.removeAllViews()
        binding.container.rowCount = h
        binding.container.columnCount = w
        for (x in 0 until w) {
            for (y in 0 until h) {
                binding.container.addView(
                    View(this).apply { setBackgroundColor(deadColor) },
                    GridLayout.LayoutParams(
                        GridLayout.spec(x, 1f),
                        GridLayout.spec(y, 1f)
                    ).apply {
                        width = 0
                        height = 0
                    }
                )
            }
        }
    }
}

fun Generation.renderInto(
    view: GridLayout,
    @ColorInt liveColor: Int = Color.GREEN,
    @ColorInt deadColor: Int = Color.BLACK
) {
    val isLive = { x: Int, y: Int -> Position(x, y) in lives }
    for (x in 0 until size.width) {
        for (y in 0 until size.height) {
            val color = if (isLive(x, y)) liveColor else deadColor
            view[y * size.width + x].setBackgroundColor(color)
        }
    }
}