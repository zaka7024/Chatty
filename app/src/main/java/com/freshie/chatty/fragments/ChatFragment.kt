package com.freshie.chatty.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelStores
import androidx.recyclerview.widget.LinearLayoutManager
import com.freshie.chatty.R
import com.freshie.chatty.fragments.viewmodels.ChatViewModel
import com.freshie.chatty.items.ChatReceiverItem
import com.freshie.chatty.items.ChatSenderItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {
    private val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = ChatFragmentArgs.fromBundle(requireArguments())
        val chatViewModel = ViewModelProviders.of(this)
            .get(ChatViewModel::class.java)

        // Set receiver id
        chatViewModel.setReceiverId(args.receiver)
        chatViewModel.getChatMessages()

        // send a message
        chat_send_icon.setOnClickListener {
            val text = chat_edittext.text.toString()

            if(text.isEmpty()) {
                // TODO::
            }
            chatViewModel.sendMessage(text)
            chat_edittext.text.clear()
        }

        initChatRv()

        // Listen to the chat messages
        chatViewModel.chatMessages.observe(viewLifecycleOwner, {
            adapter.clear()
            it.forEach {
                message ->
                adapter.add(ChatSenderItem())
            }
        })
    }

    private fun initChatRv(){
        chat_rv.adapter = adapter
        chat_rv.layoutManager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        adapter.add(ChatReceiverItem())
        adapter.add(ChatSenderItem())
        adapter.add(ChatReceiverItem())
        adapter.add(ChatSenderItem())

    }
}
