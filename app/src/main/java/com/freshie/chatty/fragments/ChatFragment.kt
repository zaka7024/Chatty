package com.freshie.chatty.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.freshie.chatty.R
import com.freshie.chatty.fragments.viewmodels.ChatViewModel
import com.freshie.chatty.items.ChatReceiverItem
import com.freshie.chatty.items.ChatSenderItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
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

        // Translator
        // Create an English-Arabic translator:
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.ARABIC)
            .build()
        val translator = Translation.getClient(options)

        // test
        chatViewModel.sourceLanguage.observe(viewLifecycleOwner, {
            Log.i("chat", "source lang: $it")
        })

        chatViewModel.targetLanguage.observe(viewLifecycleOwner, {
            Log.i("chat", "target  lang: $it")
        })


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

        val uid = Firebase.auth.uid
        // Listen to the chat messages
        chatViewModel.chatMessages.observe(viewLifecycleOwner, {
            adapter.clear()
            it.forEach { message ->
                if (message.fromId == uid) {
                    adapter.add(ChatSenderItem(message, translator))
                } else {
                    adapter.add(ChatReceiverItem(message, translator))
                }
            }
            chat_rv.scrollToPosition(adapter.itemCount - 1)
        })
    }

    private fun initChatRv(){
        chat_rv.adapter = adapter
        chat_rv.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}
