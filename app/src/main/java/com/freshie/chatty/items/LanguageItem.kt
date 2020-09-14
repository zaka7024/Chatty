package com.freshie.chatty.items

import com.freshie.chatty.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.language_item.view.*

class LanguageItem(var name :String , var icon:Int) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.language_item_flage.setImageResource(icon)
        viewHolder.itemView.language_item_name.text = name
    }

    override fun getLayout(): Int {

        return R.layout.language_item
    }
}
