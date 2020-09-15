package com.freshie.chatty.models


data class OnlineUser(var state: Boolean, var id: String, var lat: Double, var lng: Double){
    constructor(): this(false,"", 0.0, 0.0)
}