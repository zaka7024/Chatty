package com.freshie.chatty.items

import com.freshie.chatty.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item


class ChatReceiverItem :Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.message_item_receiver
    }

}