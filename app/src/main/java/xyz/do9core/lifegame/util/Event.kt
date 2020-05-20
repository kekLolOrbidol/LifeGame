package xyz.do9core.lifegame.util

import androidx.lifecycle.MutableLiveData

data class Event<out T : Any>(private val data: T) {

    var handled = false
        private set

    fun getData(): T? {
        return data.takeUnless { handled }?.also { handled = true }
    }

    fun peekData() = data
}

fun <T : Any> MutableLiveData<Event<T>>.post(data: T) = this.postValue(
    Event(data)
)

fun <T : Any> MutableLiveData<Event<T>>.set(data: T) = this.setValue(
    Event(data)
)