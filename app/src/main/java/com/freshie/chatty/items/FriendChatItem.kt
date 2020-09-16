package com.freshie.chatty.items

import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.freshie.chatty.R
import com.freshie.chatty.models.LastMessage
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.friend_item.view.*

class FriendChatItem(var message: LastMessage, var action: (String)-> Unit): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.chat_item_last_text.text = message.text
        viewHolder.itemView.chat_item_username.text = message.name

        viewHolder.itemView.setOnClickListener {
            action.invoke(message.toId)
        }

        YoYo.with(Techniques.Bounce).duration(600)
            .playOn(viewHolder.itemView)
    }

    override fun getLayout(): Int {
        return R.layout.friend_item
    }
}