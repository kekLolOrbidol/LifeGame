package xyz.do9core.lifegame

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import xyz.do9core.game.Generation
import xyz.do9core.game.Universe
import xyz.do9core.game.createUniverse
import xyz.do9core.game.life.randPoints
import xyz.do9core.lifegame.view.BooleanMatrix

class MainViewModel : ViewModel() {

    private val universe: Universe = createUniverse(51, 41, time = 20) {
        randPoints(0 to 0, 50 to 40, density = 0.1)
    }

    private val _indexedGeneration = MutableLiveData<IndexedValue<Generation>>()
    val generationCount = Transformations.map(_indexedGeneration) { it.index.toString() }
    val dataMatrix = Transformations.map(_indexedGeneration) { indexedGeneration ->
        val generation = indexedGeneration.value
        BooleanMatrix(universe.width, universe.height).also { matrix ->
            generation.lives.forEach { matrix[it.x, it.y] = true }
        }
    }

    private val _isActive = MutableLiveData(false)
    val isActive: LiveData<Boolean> = _isActive

    private var evolving: Job? = null
    fun bigBang() {
        evolving?.cancel()
        evolving = universe.asFlow()
            .buffer()
            .flowOn(Dispatchers.Default)
            .onEach { delay(500) }
            .withIndex()
            .onEach { gen -> _indexedGeneration.postValue(gen) }
            .onCompletion { _isActive.postValue(false) }
            .launchIn(viewModelScope)
        _isActive.postValue(true)
    }

    fun heatDeath() {
        evolving?.cancel()
        evolving = null
        _isActive.postValue(false)
    }
}
