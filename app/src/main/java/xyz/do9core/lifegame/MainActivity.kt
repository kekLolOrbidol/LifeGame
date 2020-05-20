package xyz.do9core.lifegame

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
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
    private val permission = ActivityResultContracts.RequestPermission()
    private val requestLauncher = CoroutineLauncher(activityResultRegistry)

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
                    viewModel.saveBitmapAsPNG(viewBitmap)
                    return@setOnClickListener
                }
                if (hasPermission) viewModel.saveBitmapAsPNG(viewBitmap) else {
                    lifecycleScope.launch {
                        val success = requestLauncher.launch(permission, WRITE_EXTERNAL_STORAGE)
                        if (success) {
                            viewModel.saveBitmapAsPNG(viewBitmap)
                        } else {
                            viewModel.snackText(getString(R.string.msg_need_permission))
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

    private val hasPermission: Boolean get() {
        val fastPath = permission.getSynchronousResult(this, WRITE_EXTERNAL_STORAGE)
            ?: return false
        return fastPath.value
    }
}
