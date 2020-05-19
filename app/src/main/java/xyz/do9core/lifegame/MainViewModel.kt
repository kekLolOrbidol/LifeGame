package xyz.do9core.lifegame

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import xyz.do9core.game.Generation
import xyz.do9core.game.Universe
import xyz.do9core.game.createUniverse
import xyz.do9core.game.life.randPoints

class MainViewModel : ViewModel() {

    private val _universe = MutableLiveData<Universe>()
    val width = Transformations.map(_universe) { it.width }
    val height = Transformations.map(_universe) { it.height }

    private val _indexedGeneration = MutableLiveData<IndexedValue<Generation>>()
    val generationCount = Transformations.map(_indexedGeneration) { it.index.inc().toString() }
    val liveCells = Transformations.map(_indexedGeneration) { it.value.lives }

    private val _isActive = MutableLiveData(false)
    val isActive: LiveData<Boolean> = _isActive

    private var evolving: Job? = null

    @ExperimentalCoroutinesApi
    fun bigBang() {
        val universe = createUniverse(41, 41, time = 10) {
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
}
