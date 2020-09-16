package com.freshie.chatty.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.freshie.chatty.R
import com.freshie.chatty.items.FriendChatItem
import com.freshie.chatty.models.Message
import com.fxn.OnBubbleClickListener
import com.google.firebase.auth.ktx.auth
import com.freshie.chatty.models.User
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Check if the user have signed or not
        val auth = Firebase.auth
        val currentUser = auth.currentUser

        // inits
        initHomeRv()
        getMessages()

        // test
        // TODO:: Remove this
        //auth.signOut()

        if(currentUser == null) {
            findNavController().navigate(R.id.signUpFragment)
        }
    }

    private fun initHomeRv() {
        home_rv.adapter = adapter
        home_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun getMessages() {
        val fromId = Firebase.auth.currentUser?.uid
        var ref = FirebaseDatabase.getInstance()
            .getReference("latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}
