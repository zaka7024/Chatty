package com.freshie.chatty.fragments.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.freshie.chatty.models.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatViewModel: ViewModel() {
    val receiverId: MutableLiveData<String> = MutableLiveData()

    fun sendMessage(text: String) {
        val fromId = Firebase.auth.uid
        val toId = receiverId.value
        val message = Message(text, fromId.toString(), receiverId.value!!)

        val db = Firebase.firestore
        db.collection("messages/$fromId/$toId").add(message)
    }
}