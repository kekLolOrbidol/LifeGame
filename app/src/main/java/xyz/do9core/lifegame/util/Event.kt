@file:Suppress("NOTHING_TO_INLINE")

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

inline fun <T : Any> MutableLiveData<Event<T>>.post(data: T) {
    this.postValue(Event(data))
}

inline fun <T : Any> MutableLiveData<Event<T>>.set(data: T) {
    this.value = Event(data)
}