package com.freshie.chatty.models

import java.util.concurrent.TimeUnit

data class Message(var text: String, var fromId: String, var toId: String, var timestamp: String
 = System.currentTimeMillis().toString()) {
    constructor(): this("", "", "")
}