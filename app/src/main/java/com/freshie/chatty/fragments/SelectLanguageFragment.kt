package com.freshie.chatty.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.freshie.chatty.R
import com.freshie.chatty.items.LanguageItem
import com.freshie.chatty.models.Language
import com.freshie.chatty.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_select_language.*
import kotlinx.coroutines.tasks.await

class SelectLanguageFragment : Fragment() {

    private var firstLanguage: Language? = null
    private var secondLanguage: Language? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLanguagesRv()
    }

    private fun initLanguagesRv() {
        val adapter = GroupAdapter<GroupieViewHolder>()
        select_language_rv.adapter = adapter
        select_language_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        for(languageType in Language.values()){
            adapter.add(LanguageItem(languageType.name, getLanguageIcon(languageType),
                languageType, ::onLanguageItemSelected, requireContext()))
        }
    }

    private fun getLanguageIcon(language: Language): Int{
        return when (language) {
            Language.Arabic -> R.drawable.ksa
            Language.English -> R.drawable.usa
            Language.France -> R.drawable.france
        }
    }

    fun onLanguageItemSelected(language: Language): Boolean{
        if(firstLanguage == null){
            firstLanguage = language
            choose_language_text.text = "Choose The Target Language"
            return true
        }else if(secondLanguage == null) {
            secondLanguage = language
            return true
        }else{
            // Done
            return false
        }
    }

    private fun saveCurrentUserLanguages() {
        //val user = getCurrentUser()
    }

    private suspend fun getCurrentUser(): User {
        val db = Firebase.firestore
        val auth = Firebase.auth
        val doc = db.collection("users").whereEqualTo("id", auth.currentUser?.uid)
            .limit(1).get().await()
        return doc.first().toObject<User>()
    }
}
