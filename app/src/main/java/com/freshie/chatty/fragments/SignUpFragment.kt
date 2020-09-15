package com.freshie.chatty.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.freshie.chatty.R
import com.freshie.chatty.models.OnlineUser
import com.freshie.chatty.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sign up
        sign_up_btn.setOnClickListener {
            signUp()
        }

        // Sing in
        sign_in_text.setOnClickListener {
            navigateToSignIn()
        }
    }

    private fun signUp() {
        val auth = Firebase.auth
        val email = email_edittext.text.toString()
        val username = username_edittext.text.toString()
        val password = password_edittext.text.toString()

        if(email.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(context, "Please fill all information", Toast.LENGTH_SHORT)
                .show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Authentication success.",
                    Toast.LENGTH_SHORT).show()
                // Save the user
                createUser(username, auth.currentUser!!.uid)

            } else {
                Toast.makeText(context, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSelectLanguage())
    }

    private fun navigateToSignIn() {
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
    }

    private fun createUser(name: String, id: String) {
        val user = User(name, id)
        // Save the user to firebase
        val db = Firebase.firestore
        db.collection("users")
            .add(user)
        // add to online users
        db.collection("online").add(OnlineUser(true, user.id, 0.0, 0.0))
            .addOnSuccessListener {
                navigateToHome()
            }
    }
}
