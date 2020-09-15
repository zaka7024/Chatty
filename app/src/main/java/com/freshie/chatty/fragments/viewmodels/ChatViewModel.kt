package com.freshie.chatty.fragments.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.freshie.chatty.models.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ChatViewModel: ViewModel() {
    val receiverId: MutableLiveData<String> = MutableLiveData()
    private val _chatMessages: MutableLiveData<MutableList<Message>> = MutableLiveData()
    val chatMessages: LiveData<MutableList<Message>>
        get() = _chatMessages

    init {
    }

    fun setReceiverId(id: String){
        receiverId.value = id
    }

    fun sendMessage(text: String) {
        val fromId = Firebase.auth.uid
        val toId = receiverId.value
        val message = Message(text, fromId.toString(), receiverId.value!!)

        val db = Firebase.firestore
        db.collection("messages/$fromId/$toId").add(message)

        db.collection("messages/$toId/$fromId").add(message)
    }

    fun getChatMessages() {
        val fromId = Firebase.auth.uid
        val toId = receiverId.value

        val db = Firebase.firestore
        db.collection("messages").document("$fromId")
            .collection("$toId").addSnapshotListener { docs, e ->

            if (e != null) {
                return@addSnapshotListener
            }

            if (docs != null) {
                val messagesList = mutableListOf<Message>()
                for(doc in docs){
                    val message = doc.toObject<Message>()
                    messagesList.add(message)
                }

                _chatMessages.value = messagesList

                //val message = snapshot.toObject<Message>()
                //_chatMessages.value?.add(message!!)
            }
        }
    }
}