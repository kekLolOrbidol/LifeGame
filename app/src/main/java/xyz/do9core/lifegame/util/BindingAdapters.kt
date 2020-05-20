package xyz.do9core.lifegame.util

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.BindingAdapter
import com.google.android.material.snackbar.Snackbar

data class SnackbarData(
    val text: CharSequence,
    val actionName: String? = null,
    val onAction: (() -> Unit)? = null
)

typealias SnackbarEvent = Event<SnackbarData>

@BindingAdapter("snackbar")
fun CoordinatorLayout.bindSnackbarEvent(event: SnackbarEvent?) {
    event?.getData()?.let { data ->
        val snack = Snackbar.make(this, data.text, Snackbar.LENGTH_SHORT)
        data.actionName?.let {
            snack.setAction(it) {
                data.onAction?.invoke()
            }
        }
        snack.show()
    }
}