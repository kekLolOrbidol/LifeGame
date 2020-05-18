package xyz.do9core.lifegame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import xyz.do9core.game.Generation
import xyz.do9core.game.Universe
import xyz.do9core.game.createUniverse
import xyz.do9core.game.life.ring

class MainViewModel : ViewModel() {

    val universe: Universe = createUniverse(51, 51) {
        ring(8, 8)
    }

    private val _generation = MutableLiveData<Generation>()
    val generation: LiveData<Generation> = _generation

    val isActive get() = evolving?.isActive ?: false

    private var evolving: Job? = null
    fun bigBang() {
        evolving?.cancel()
        evolving = universe.asFlow()
            .buffer()
            .flowOn(Dispatchers.Default)
            .onEach { delay(500) }
            .onEach { gen -> _generation.postValue(gen) }
            .launchIn(viewModelScope)
    }

    fun heatDeath() {
        evolving?.cancel()
        evolving = null
    }
}
