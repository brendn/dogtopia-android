package dog.brendan.myapplication

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Spinner

inline fun Spinner.onSelect(crossinline action: (pos: Int) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
            action(position)
        }
        override fun onNothingSelected(parentView: AdapterView<*>) {}
    }
}



fun Context.fadeIn(view: View, offset: Long = 0L) {
    val anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
    anim.reset()
    view.clearAnimation()
    view.visibility = View.VISIBLE
    anim.startOffset = offset
    view.startAnimation(anim)
}

fun Context.fadeOutIn(view: View, offset: Long = 0L) {
    val anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
    anim.reset()
    view.clearAnimation()
    view.visibility = View.VISIBLE
    anim.startOffset = offset
    view.startAnimation(anim)
    fadeIn(view)
}