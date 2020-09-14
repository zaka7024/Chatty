package com.freshie.chatty.models

enum class Language {
    English, Arabic, Turkey, France
}

data class User(var name: String, var id: String, var motherLanguage: Language? = null,
    var targetLanguage: Language? = null){
    constructor(): this("", "", null, null)
}