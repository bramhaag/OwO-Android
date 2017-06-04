package me.bramhaag.owoandroid.listeners

import android.view.View
import me.bramhaag.owoandroid.R
import net.cachapa.expandablelayout.ExpandableLayout

class ExpandableLayoutListener(val expandableLayoutUpload: ExpandableLayout, val expandableLayoutShorten: ExpandableLayout) : View.OnClickListener {

    override fun onClick(view: View) {
        if (view.id == R.id.button_expand_upload) {
            if(expandableLayoutUpload.isExpanded) {
                expandableLayoutUpload.collapse()
                return
            }

            expandableLayoutUpload.expand()
            expandableLayoutShorten.collapse()
        } else if (view.id == R.id.button_expand_shorten) {
            if(expandableLayoutShorten.isExpanded) {
                expandableLayoutShorten.collapse()
                return
            }

            expandableLayoutUpload.collapse()
            expandableLayoutShorten.expand()
        }
    }
}

