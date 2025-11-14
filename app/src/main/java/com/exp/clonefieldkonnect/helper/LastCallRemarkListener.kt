package com.exp.clonefieldkonnect.helper

import com.exp.clonefieldkonnect.model.LastCallRemarkModel

interface LastCallRemarkListener {
    fun onLastCallRemarkRequired(data: LastCallRemarkModel.Data)
}
