package com.freshie.chatty.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.freshie.chatty.R
import com.freshie.chatty.fragments.viewmodels.ProfileViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_dicscover.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.map_tip
import kotlinx.android.synthetic.main.fragment_profile.map_tip_text

class ProfileFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    private fun hideAllViews() {
        map_tip_text.visibility = View.VISIBLE
        profile_image.visibility = View.INVISIBLE
        profile_name.visibility = View.INVISIBLE
        textView9.visibility = View.INVISIBLE
        motehr_language.visibility = View.INVISIBLE
        textView11.visibility = View.INVISIBLE
        target_language.visibility = View.INVISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Firebase.auth.currentUser?.uid == null) {
            map_tip.visibility = View.VISIBLE
            hideAllViews()
            return
        }

        val profileViewModel = ViewModelProviders.of(this)
            .get(ProfileViewModel::class.java)

        profileViewModel.currentUser.observe(viewLifecycleOwner, {
            currentUser ->
            if(currentUser != null){
                profile_name.text = currentUser.name
                motehr_language.text = currentUser.motherLanguage?.name
                target_language.text = currentUser.targetLanguage?.name
            }
        })
    }

}
