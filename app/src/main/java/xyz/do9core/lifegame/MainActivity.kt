package xyz.do9core.lifegame

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import xyz.do9core.game.Generation
import xyz.do9core.game.Universe
import xyz.do9core.lifegame.databinding.ActivityMainBinding
import xyz.do9core.lifegame.view.BooleanMatrix

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.runButton) {
            setOnClickListener {
                if (viewModel.isActive) {
                    viewModel.heatDeath()
                    setText(R.string.btn_restart)
                } else {
                    viewModel.bigBang()
                    setText(R.string.btn_stop)
                }
            }
        }

        viewModel.generation.observe(this, Observer {
            render(viewModel.universe, it)
        })
    }

    private fun render(universe: Universe, gen: Generation) {
        val matrix = BooleanMatrix(
            universe.width,
            universe.height
        ).also { matrix ->
            gen.lives.forEach { matrix[it.x, it.y] = true }
        }
        binding.container.setMatrix(matrix)
    }
}
