package com.freshie.chatty.items

import com.freshie.chatty.R
import com.freshie.chatty.models.User
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class FriendChatItem(var user: User): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        
    }

    override fun getLayout(): Int {
        return R.layout.friend_item
    }
}