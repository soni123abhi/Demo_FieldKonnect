package com.exp.clonefieldkonnect.helper

import android.view.View

interface ClickListener {


    abstract fun onClick(view: View, position: Int)

    abstract fun onLongClick(view: View, position: Int)
}