package com.freshie.chatty.items

import android.content.Context
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.freshie.chatty.R
import com.freshie.chatty.models.Language
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.language_item.view.*

class LanguageItem  (var name :String , var icon:Int, val language: Language
, var action: (Language)->Boolean, var context: Context) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.language_item_flage.setImageResource(icon)
        viewHolder.itemView.language_item_name.text = name

        viewHolder.itemView.setOnClickListener {
            // Invoke
            val select = action.invoke(language)
            if(select) {
                viewHolder.itemView.setBackgroundResource(R.color.darkBlue)
                val color = context.resources.getColor(R.color.white)
                viewHolder.itemView.language_item_name.setTextColor(color)
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.language_item
    }
}
