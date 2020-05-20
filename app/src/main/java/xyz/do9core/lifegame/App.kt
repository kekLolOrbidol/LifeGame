package xyz.do9core.lifegame

import android.app.Application
import kotlinx.coroutines.MainScope

class App : Application() {

    internal val coroutineScope = MainScope()
}
