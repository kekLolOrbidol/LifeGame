package xyz.do9core.lifegame

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import xyz.do9core.lifegame.databinding.ActivityMainBinding
import xyz.do9core.lifegame.util.CoroutineLauncher

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private val requestPermission =
        CoroutineLauncher(activityResultRegistry)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main
        ).also { binding ->
            binding.lifecycleOwner = this
            binding.viewModel = viewModel
            binding.snapshotButton.setOnClickListener {
                val viewBitmap = binding.container.snapshot()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    viewModel.savePNG(viewBitmap)
                    return@setOnClickListener
                }
                if (hasPermission) viewModel.savePNG(viewBitmap) else {
                    lifecycleScope.launch {
                        val success = requestPermission.launch(
                            contract = ActivityResultContracts.RequestPermission(),
                            input = Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        if (success) {
                            viewModel.savePNG(viewBitmap)
                        } else {
                            viewModel.snackText("Snapshot need your permission to save.")
                        }
                    }
                }
            }
        }

        viewModel.savedImage.observe(this, Observer { event ->
            event?.getData()?.let { imageUri ->
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(imageUri, "image/png")
                }
                startActivity(intent)
            }
        })
    }

    private val hasPermission
        get() =
            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED
}
