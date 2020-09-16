package com.freshie.chatty.fragments.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.freshie.chatty.models.Language
import com.freshie.chatty.models.Message
import com.freshie.chatty.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ChatViewModel: ViewModel() {
    val receiverId: MutableLiveData<String> = MutableLiveData()

    private val _chatMessages: MutableLiveData<MutableList<Message>> = MutableLiveData()
    val chatMessages: LiveData<MutableList<Message>>
        get() = _chatMessages

    private val _sourceLanguage: MutableLiveData<Language> = MutableLiveData()
    val sourceLanguage: LiveData<Language>
        get() = _sourceLanguage

    private val _targetLanguage: MutableLiveData<Language> = MutableLiveData()
    val targetLanguage: LiveData<Language>
        get() = _targetLanguage

    private val _friendUser: MutableLiveData<User> = MutableLiveData()
    val friendUser: LiveData<User>
        get() = _friendUser

    private val _currentUser: MutableLiveData<User> = MutableLiveData()
    val currentUser: LiveData<User>
        get() = _currentUser

    init {
         getUser()
    }

    private fun getUser() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                _currentUser.value = getCurrentUser()
                setSourceLanguage(_currentUser.value?.motherLanguage!!)

                // get friend user
                setReceiverId(receiverId.value!!)
            }
        }
    }

    fun setReceiverId(id: String){
        receiverId.value = id

        // get the friend user
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                _friendUser.value = getFriendUser()
                setTargetLanguage(_friendUser.value?.motherLanguage!!)
            }
        }
    }

    private fun setSourceLanguage(language: Language) {
        _sourceLanguage.value = language
    }

    private fun setTargetLanguage(language: Language) {
        _targetLanguage.value = language
    }

    private suspend fun getCurrentUser(): User?{
        val db = Firebase.firestore
        val id = Firebase.auth.currentUser?.uid
        return db.collection("users").document(id.toString())
            .get().await().toObject<User>()
    }

    private suspend fun getFriendUser(): User?{
        val db = Firebase.firestore
        val id = receiverId.value
        return db.collection("users").document(id.toString())
            .get().await().toObject<User>()
    }

    fun sendMessage(text: String) {
        val fromId = Firebase.auth.uid
        val toId = receiverId.value
        val message = Message(text, fromId.toString(), receiverId.value!!)

        val db = Firebase.firestore
        db.collection("messages/$fromId/$toId").add(message)

        db.collection("messages/$toId/$fromId").add(message)

        val lastTo = FirebaseDatabase.getInstance().getReference("latest-messages/$fromId/$toId")
        val lastFrom = FirebaseDatabase.getInstance().getReference("latest-messages/$fromId/$toId")
        lastTo.setValue(message)
        lastFrom.setValue(message)

    }

    fun getChatMessages() {
        val fromId = Firebase.auth.uid
        val toId = receiverId.value

        val db = Firebase.firestore
        db.collection("messages").document("$fromId")
            .collection("$toId").orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener { docs, e ->

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
            }
        }
    }
}