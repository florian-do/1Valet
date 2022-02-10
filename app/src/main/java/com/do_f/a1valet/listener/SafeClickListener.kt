package com.do_f.a1valet.listener

import android.os.SystemClock
import android.view.View

class SafeClickListener(
    private var defaultInterval: Int = 1000,
    private var safeClick: (View) -> Unit): View.OnClickListener {

    private var lastTimeClicked = 0L

    override fun onClick(p0: View) {
        if (SystemClock.elapsedRealtime() - defaultInterval < lastTimeClicked) {
            return
        }

        lastTimeClicked = SystemClock.elapsedRealtime()
        safeClick(p0)
    }
}