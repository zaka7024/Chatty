package com.freshie.chatty.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.freshie.chatty.R
import com.freshie.chatty.items.FriendChatItem
import com.freshie.chatty.models.LastMessage
import com.freshie.chatty.models.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_dicscover.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val adapter = GroupAdapter<GroupieViewHolder>()
    var lastMessageMap = HashMap<String?, LastMessage>()

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

        if(currentUser == null) {
            findNavController().navigate(R.id.signUpFragment)
        }

        // init
        initHomeRv()
        getMessages()

        // TODO:: Remove this
        auth.signOut()
    }

    private fun initHomeRv() {
        home_rv.adapter = adapter
        home_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun navigateToChatFragment(targetUserId: String) {
        findNavController().navigate(HomeFragmentDirections
            .actionHomeFragmentToChatFragment(targetUserId))
    }

    private fun refreshRv() {
        adapter.clear()
        lastMessageMap.forEach {
            adapter.add(FriendChatItem(it.value, ::navigateToChatFragment))
        }
    }

    private fun getMessages() {
        val fromId = Firebase.auth.currentUser?.uid
        var ref = FirebaseDatabase.getInstance()
            .getReference("latest-messages/$fromId")

        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(LastMessage::class.java)
                lastMessageMap[snapshot.key] = message!!
                refreshRv()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(LastMessage::class.java)
                lastMessageMap[snapshot.key] = message!!
                refreshRv()
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
