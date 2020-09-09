package xyz.do9core.lifegame.util

import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityOptionsCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class CoroutineLaunch(private val registry: ActivityResultRegistry) {

    companion object {
        private const val TAG = "xyz.do9core.SuspendLauncher"
    }

    suspend fun <I, O> launch(
        contract: ActivityResultContract<I, O>,
        input: I,
        options: ActivityOptionsCompat? = null
    ): O = suspendCancellableCoroutine { cont ->
        val launcher = registry.register(TAG, contract) { result ->
            cont.resume(result)
            cont.cancel()
        }
        cont.invokeOnCancellation { launcher.unregister() }
        launcher.launch(input, options)
    }
}
