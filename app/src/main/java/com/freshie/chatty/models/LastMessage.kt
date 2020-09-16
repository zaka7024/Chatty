package com.freshie.chatty.models

data class LastMessage(var text: String, var toId: String, var name: String){
    constructor(): this("","","")
}