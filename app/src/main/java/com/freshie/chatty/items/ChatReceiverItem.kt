package com.freshie.chatty.items

import android.util.Log
import com.freshie.chatty.R
import com.freshie.chatty.models.Message
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translator
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.message_item_receiver.view.*
import kotlinx.android.synthetic.main.message_item_receiver.view.tans_messge_item_receiver
import kotlinx.android.synthetic.main.message_item_sender.view.*


class ChatReceiverItem(var message: Message, var translator: Translator): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_item_receiver_text.text = message.text

        var conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translator.translate(message.text)
                    .addOnSuccessListener { translatedText ->
                        // Translation successful.
                        Log.i("chatItem", "translated: $translatedText")
                        viewHolder.itemView.tans_messge_item_receiver.text = translatedText
                    }
            }
    }

    override fun getLayout(): Int {
        return R.layout.message_item_receiver
    }

}