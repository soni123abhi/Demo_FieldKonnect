package com.exp.clonefieldkonnect.helper

interface CallStatusListener {
    fun onCallEnded(number: String, duration: Int, callStatus: Int, dateTime: String)
}