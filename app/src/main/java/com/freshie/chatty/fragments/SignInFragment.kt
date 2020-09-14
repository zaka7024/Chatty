package com.freshie.chatty.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.freshie.chatty.R
import com.freshie.chatty.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.email_edittext
import kotlinx.android.synthetic.main.fragment_sign_in.password_edittext
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignInFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sign_in_btn.setOnClickListener {
            signIn()
        }

        sign_up_text.setOnClickListener {
            navigateToSignUp()
        }
    }

    private fun signIn() {
        val auth = Firebase.auth
        val email = email_edittext.text.toString()
        val password = password_edittext.text.toString()

        if(email.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(context, "Please fill all information", Toast.LENGTH_SHORT)
                .show()
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Authentication success.",
                    Toast.LENGTH_SHORT).show()


            } else {
                Toast.makeText(context, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToSignUp() {
        findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
    }
}
