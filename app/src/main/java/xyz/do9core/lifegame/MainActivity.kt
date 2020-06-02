package xyz.do9core.lifegame

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.do9core.lifegame.databinding.ActivityMainBinding
import xyz.do9core.lifegame.util.CoroutineLauncher
import xyz.do9core.lifegame.util.dataBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by dataBinding()
    private val viewModel: MainViewModel by viewModels()
    private val permission = ActivityResultContracts.RequestPermission()
    private val requestLauncher = CoroutineLauncher(activityResultRegistry)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.snapshotButton.setOnClickListener {
            val viewBitmap = binding.container.snapshot()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                viewModel.saveBitmapAsPNG(viewBitmap)
                return@setOnClickListener
            }
            if (hasPermission) {
                viewModel.saveBitmapAsPNG(viewBitmap)
                return@setOnClickListener
            }
            fun checkPermissionResult(success: Boolean) {
                if (success) {
                    viewModel.saveBitmapAsPNG(viewBitmap)
                } else {
                    viewModel.snackText(getString(R.string.msg_need_permission))
                }
            }
            lifecycleScope.launch(Dispatchers.Main) {
                val result = requestLauncher.launch(permission, WRITE_EXTERNAL_STORAGE)
                checkPermissionResult(result)
            }
        }
        binding.container.setOnClickListener {
            val transitionOption = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, binding.container, binding.container.transitionName)
            val intent = Intent(this, ScaleActivity::class.java).apply {
                val data = ScaleParameters(
                    width = viewModel.width.value ?: 1,
                    height = viewModel.height.value ?: 1,
                    points = viewModel.liveCells.value.orEmpty()
                )
                putExtra(KEY_SCALE_PARAMETERS, data)
            }
            startActivity(intent, transitionOption.toBundle())
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
