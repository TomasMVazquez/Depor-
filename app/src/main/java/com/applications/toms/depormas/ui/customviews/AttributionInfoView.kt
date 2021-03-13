package com.applications.toms.depormas.ui.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.buildSpannedString

class AttributionInfoView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
): AppCompatTextView(context,attrs,defStyleAttr) {

    fun setAttributions(attributions: Array<CharSequence>){
        text = buildSpannedString {
            attributions.forEach {
                appendLine(it)
                appendLine()
            }
        }
    }

}