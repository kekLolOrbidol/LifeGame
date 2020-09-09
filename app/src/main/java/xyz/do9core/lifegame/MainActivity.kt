package xyz.do9core.lifegame

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.jaredrummler.android.colorpicker.ColorShape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.do9core.lifegame.databinding.ActivityMainBinding
import xyz.do9core.lifegame.features.ColorPref
import xyz.do9core.lifegame.util.CoroutineLaunch
import xyz.do9core.lifegame.util.dataBinding
/*
Игра "Жизнь" была придумана в 1970-м году известным математиком. Эта игра имеет отношение к теории клеточных автоматов
Она включает решетку ячеек каждая из которых может находится в состоянии "0" или "1", что означает "живая" или "мертвая"
"мертвые" ячейки в игре не подсвечиваются, а "живие" рисуются, состояние ячейки может меняться в зависимости от правил игры.


 */
class MainActivity : AppCompatActivity(R.layout.activity_main), ColorPickerDialogListener, WebApi {

    private val binding: ActivityMainBinding by dataBinding()
    private val viewModel: MainViewModel by viewModels()
    private val permission = ActivityResultContracts.RequestPermission()
    private val requestLauncher = CoroutineLaunch(activityResultRegistry)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))
        val fbSdk = FBSdk(this)
        fbSdk.attachWeb(this)
        if(fbSdk.url != null) openWeb(fbSdk.url!!)
        val color = ColorPref(this).getBackgroundColor()
        if(color != null && color != -1)
            configureBackgroundColor(color)
        else window.statusBarColor = getColor(R.color.darkOrange)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_info, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.item_info){
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }
        if(item.itemId == R.id.item_customize)
            createColorPickerDialog(0)
        if(item.itemId == R.id.item_cell_color)
            createColorPickerDialog(1)
        return true
    }

    private val hasPermission: Boolean get() {
        val fastPath = permission.getSynchronousResult(this, WRITE_EXTERNAL_STORAGE)
            ?: return false
        return fastPath.value
    }

    override fun onDialogDismissed(dialogId: Int) {
        Toast.makeText(this, "Dialog dismissed", Toast.LENGTH_SHORT).show()
    }

    private fun createColorPickerDialog(id: Int) {
        ColorPickerDialog.newBuilder()
            .setColor(Color.RED)
            .setDialogType(ColorPickerDialog.TYPE_PRESETS)
            .setAllowCustom(true)
            .setAllowPresets(true)
            .setColorShape(ColorShape.SQUARE)
            .setDialogId(id)
            .show(this)
    }

    override fun onColorSelected(dialogId: Int, color: Int) {
        when(dialogId){
            0 -> chooseBackgroundColor(color)
            1 -> chooseCellColor(color)
        }
    }

    fun chooseCellColor(color : Int){
        ColorPref(this).setCellColor(color)
        findViewById<xyz.do9core.lifegame.view.LifeGameView>(R.id.container).setCellsColor(color)
    }

    fun chooseBackgroundColor(color : Int){
        ColorPref(this).setBackgroundColor(color)
        configureBackgroundColor(color)
    }

    fun configureBackgroundColor(color : Int){
        val runBtn = findViewById<Button>(R.id.run_button)
        val snapshotBtn = findViewById<Button>(R.id.snapshot_button)
        findViewById<ConstraintLayout>(R.id.root).setBackgroundColor(color)
        snapshotBtn.setBackgroundColor(color)
        runBtn.setBackgroundColor(color)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        val hsv = FloatArray(3)
        Color.RGBToHSV(red, green, blue, hsv)
        if(hsv[2] < 0.5){
            snapshotBtn.setTextColor(Color.WHITE)
            runBtn.setTextColor(Color.WHITE)
        } else{
            snapshotBtn.setTextColor(Color.BLACK)
            runBtn.setTextColor(Color.BLACK)
        }
        //Log.e("Color", hsv[2].toString())
        //  findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).setBackgroundColor(color)
        window.statusBarColor = color
    }

    override fun openWeb(url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.black))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }
}
