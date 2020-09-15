package com.freshie.chatty.fragments.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.freshie.chatty.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileViewModel : ViewModel() {

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
            }
        }
    }

    private suspend fun getCurrentUser(): User?{
        val db = Firebase.firestore
        val id = Firebase.auth.currentUser?.uid
        return db.collection("users").document(id.toString())
            .get().await().toObject<User>()
    }
}