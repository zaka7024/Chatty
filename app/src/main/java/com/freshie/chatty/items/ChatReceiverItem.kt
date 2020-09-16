package com.freshie.chatty.items

import android.util.Log
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.freshie.chatty.R
import com.freshie.chatty.models.Message
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translator
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.message_item_receiver.view.*
import kotlinx.android.synthetic.main.message_item_receiver.view.message_receiver_item_translated_text


class ChatReceiverItem(var message: Message, var translator: Translator): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_item_receiver_text.text = message.text

        var conditions = DownloadConditions.Builder()
            .build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translator.translate(message.text)
                    .addOnSuccessListener { translatedText ->
                        // Translation successful.
                        Log.i("chatItem", "translated: $translatedText")
                        viewHolder.itemView.message_receiver_item_translated_text.text = translatedText
                    }
            }.addOnFailureListener {
                translator.close()
            }
    }

    override fun getLayout(): Int {
        return R.layout.message_item_receiver
    }
}