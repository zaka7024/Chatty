package com.freshie.chatty.models

data class User(var name: String, var id: String){
    constructor(): this("", "")
}