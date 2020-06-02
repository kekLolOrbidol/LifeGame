package xyz.do9core.lifegame.util

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private typealias ViewBinder<T> = (View) -> T

private class ViewBindingDelegate<T : ViewBinding>(
    private val viewBinder: ViewBinder<T>
) : ReadOnlyProperty<Activity, T> {

    private var cached: T? = null

    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return cached ?: createBinding(thisRef).also { cached = it }
    }

    private fun createBinding(activity: Activity): T {
        val contentView = activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        return viewBinder(contentView)
    }
}

fun <T : ViewBinding> Activity.viewBinding(
    viewBinder: ViewBinder<T>
): ReadOnlyProperty<Activity, T> = ViewBindingDelegate(viewBinder)

fun <T : ViewDataBinding> Activity.dataBinding(
    viewBinder: ViewBinder<T> = { requireNotNull(DataBindingUtil.bind<T>(it)) }
): ReadOnlyProperty<Activity, T> = ViewBindingDelegate(viewBinder)
