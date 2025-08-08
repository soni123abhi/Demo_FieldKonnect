package com.exp.clonefieldkonnect.model

class PointCollectionRequest {

    var collection: ArrayList<Collection>? = null
    var checkinid: String? = null
    var customer_id: String? = null

    class Collection {
        var points : Int? = null
        var point_type : String? = null
        var quantity : String? = null

    }
}