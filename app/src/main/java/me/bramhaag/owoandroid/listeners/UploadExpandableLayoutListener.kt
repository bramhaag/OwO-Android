package me.bramhaag.owoandroid.listeners

import android.view.View
import net.cachapa.expandablelayout.ExpandableLayout

class UploadExpandableLayoutListener(val uploadExpandableLayout: ExpandableLayout, val shortenExpandableLayout: ExpandableLayout): View.OnClickListener {

    override fun onClick(v: View) {
        uploadExpandableLayout.let {
            if(it.isExpanded) {
                it.collapse()
            } else {
                it.expand()
                if(shortenExpandableLayout.isExpanded) shortenExpandableLayout.collapse()
            }
        }

    }
}


