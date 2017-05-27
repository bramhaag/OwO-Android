package me.bramhaag.owoandroid.listeners

import android.view.View
import net.cachapa.expandablelayout.ExpandableLayout

class ShortenExpandableLayoutListener(val uploadExpandableLayout: ExpandableLayout, val shortenExpandableLayout: ExpandableLayout): View.OnClickListener {

    override fun onClick(v: View) {
        shortenExpandableLayout.let {
            if(it.isExpanded) {
                it.collapse()
            } else {
                it.expand()
                if(uploadExpandableLayout.isExpanded) uploadExpandableLayout.collapse()
            }
        }

    }
}