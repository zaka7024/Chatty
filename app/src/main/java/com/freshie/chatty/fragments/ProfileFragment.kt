package com.freshie.chatty.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.freshie.chatty.R
import com.freshie.chatty.fragments.viewmodels.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
