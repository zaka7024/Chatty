package com.freshie.chatty.items

import com.freshie.chatty.R
import com.freshie.chatty.models.Message
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.message_item_sender.view.*


class ChatSenderItem(var message: Message) :Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_item_text.text = message.text

    }

    override fun getLayout(): Int {
        return R.layout.message_item_sender
    }

    fun translate() {

    }

}