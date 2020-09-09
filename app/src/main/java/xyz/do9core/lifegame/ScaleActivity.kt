package xyz.do9core.lifegame

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.parcel.Parcelize
import xyz.do9core.game.Point
import xyz.do9core.lifegame.databinding.ActivityScaleBinding
import xyz.do9core.lifegame.features.ColorPref
import xyz.do9core.lifegame.util.viewBinding

const val KEY_SCALE_PARAMETERS = "SCALE_PARAMETERS"

@Parcelize
data class ScaleParameters(
    val width: Int,
    val height: Int,
    val points: Set<Point>
) : Parcelable

class ScaleActivity : AppCompatActivity(R.layout.activity_scale) {

    private val binding by viewBinding { ActivityScaleBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val color = ColorPref(this).getBackgroundColor()
        if(color != null && color != -1){
            findViewById<ConstraintLayout>(R.id.scale_root).setBackgroundColor(color)
            window.statusBarColor = color
        }
        binding.root.setOnClickListener {
            supportFinishAfterTransition()
        }
    }

    override fun onStart() {
        super.onStart()
        val data = intent.extras?.getParcelable<ScaleParameters>(KEY_SCALE_PARAMETERS) ?: return
        with(binding) {
            monitor.columns = data.width
            monitor.rows = data.height
            monitor.post {
                monitor.setLives(data.points)
            }
        }
    }
}