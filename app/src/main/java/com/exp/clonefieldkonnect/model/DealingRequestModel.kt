package com.exp.clonefieldkonnect.model

class DealingRequestModel {

    var types = ""
    var segments = ""
    var hcv = ""
    var mav = ""
    var lcv = ""
    var lmv = ""
    var other = ""
    var tractor = ""



    constructor(types: String, hcv: String, mav: String, lcv: String, other: String) {
        this.types = types
        this.hcv = hcv
        this.mav = mav
        this.lcv = lcv
        this.other = other
    }

    constructor()
}