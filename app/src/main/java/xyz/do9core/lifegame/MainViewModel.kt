package xyz.do9core.lifegame

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.contentValuesOf
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import xyz.do9core.game.Generation
import xyz.do9core.game.Universe
import xyz.do9core.game.createUniverse
import xyz.do9core.game.life.randPoints
import xyz.do9core.lifegame.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _universe = MutableLiveData<Universe>()
    val width = _universe.map { it.width }.distinctUntilChanged()
    val height = _universe.map { it.height }.distinctUntilChanged()

    private val _indexedGeneration = MutableLiveData<IndexedValue<Generation>>()
    val generationCount = _indexedGeneration.map { it.index.inc().toString() }
    val liveCells = _indexedGeneration.map { it.value.lives }

    private val _isActive = MutableLiveData(false)
    val isActive: LiveData<Boolean> = _isActive

    private val _snackEvent = MutableLiveData<SnackbarEvent>()
    val snackEvent: LiveData<SnackbarEvent> = _snackEvent

    private val _savedImage = MutableLiveData<Event<Uri>>()
    val savedImage: LiveData<Event<Uri>> = _savedImage

    private var evolving: Job? = null

    @ExperimentalCoroutinesApi
    fun bigBang() {
        val universe = createUniverse(41, 41) {
            randPoints(0 to 0, 40 to 40, density = 0.1)
        }
        _universe.postValue(universe)
        evolving = universe.asFlow()
            .buffer()
            .onEach { delay(500) }
            .withIndex()
            .flowOn(Dispatchers.Default)
            .onStart { _isActive.value = true }
            .onEach { _indexedGeneration.value = it }
            .onCompletion { _isActive.value = false }
            .launchIn(viewModelScope)
    }

    fun heatDeath() {
        evolving?.cancel()
        evolving = null
    }

    fun snackText(text: CharSequence) {
        _snackEvent.set(SnackbarData(text))
    }

    fun saveBitmapAsPNG(bitmap: Bitmap) {
        fun getName() = "lifegame-snapshot-${System.currentTimeMillis()}"
        val app = getApplication<App>()
        val resolver = app.contentResolver
        // Keep the task run beyond viewModelScope, we need the image to be saved.
        app.coroutineScope.launch(Dispatchers.IO) {
            val contentValues = contentValuesOf(
                MediaStore.MediaColumns.DISPLAY_NAME to getName(),
                MediaStore.MediaColumns.MIME_TYPE to "image/png"
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "${Environment.DIRECTORY_PICTURES}/LifeGame"
                )
            }
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            val snack = if (uri == null) {
                SnackbarData(app.getString(R.string.msg_save_snapshot_failed))
            } else {
                resolver.openOutputStream(uri).use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
                SnackbarData("Snapshot saved.", "Open") { _savedImage.post(uri) }
            }
            _snackEvent.post(snack)
        }
    }
}
