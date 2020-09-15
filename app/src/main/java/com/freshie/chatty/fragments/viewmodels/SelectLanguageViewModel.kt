package com.freshie.chatty.fragments.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.freshie.chatty.models.Language
import com.freshie.chatty.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class SelectLanguageViewModel : ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    private var _firstLanguage: MutableLiveData<Language?> = MutableLiveData()
    val firstLanguage: LiveData<Language?>
        get() = _firstLanguage
    private var _secondLanguage: MutableLiveData<Language?> = MutableLiveData()
    val secondLanguage: LiveData<Language?>
        get() = _secondLanguage

    fun onLanguageItemSelected(language: Language): Boolean{
        return when {
            _firstLanguage.value == null -> {
                _firstLanguage.value = language
                true
            }
            _secondLanguage.value == null -> {
                _secondLanguage.value = language
                true
            }
            else -> {
                false
            }
        }
    }

    fun saveCurrentUserLanguages() {
        scope.launch {
            val db = Firebase.firestore
            val auth = Firebase.auth
            val ref = db.collection("users").whereEqualTo("id", auth.currentUser?.uid)
                .limit(1).get().await().documents.first().reference
            ref.update("motherLanguage", _firstLanguage.value)
            ref.update("targetLanguage", _secondLanguage.value)
        }
    }
}