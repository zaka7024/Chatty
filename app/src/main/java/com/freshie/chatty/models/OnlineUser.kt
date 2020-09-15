package com.freshie.chatty.models

import com.google.type.LatLng

data class OnlineUser(var state: Boolean, var id: String, var latLng: LatLng?){
    constructor(): this(false,"", null)
}