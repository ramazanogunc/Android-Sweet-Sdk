package com.ramo.core.demo.customview

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import com.ramo.core.demo.R

class LoadingDialog(context: Context) : Dialog(context) {
    init {
        setContentView(R.layout.layout_loading)
        window?.setBackgroundDrawable(ColorDrawable(0))
    }
}