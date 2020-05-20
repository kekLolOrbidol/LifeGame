package xyz.do9core.lifegame.util

import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityOptionsCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class CoroutineLauncher(private val activityResultRegistry: ActivityResultRegistry) {

    companion object {
        const val TAG = "SuspendLauncher"
    }

    suspend fun <I, O> launch(
        contract: ActivityResultContract<I, O>,
        input: I,
        options: ActivityOptionsCompat? = null
    ): O = suspendCancellableCoroutine { cont ->
        val launcher = activityResultRegistry.register(TAG, contract) { result ->
            cont.resume(result)
        }
        cont.invokeOnCancellation { launcher.unregister() }
        launcher.launch(input, options)
    }
}
