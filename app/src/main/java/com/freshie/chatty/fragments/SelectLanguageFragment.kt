package com.freshie.chatty.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.freshie.chatty.R
import com.freshie.chatty.fragments.viewmodels.SelectLanguageViewModel
import com.freshie.chatty.items.LanguageItem
import com.freshie.chatty.models.Language
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_select_language.*

class SelectLanguageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectLanguageViewModel = ViewModelProviders.of(this)
            .get(SelectLanguageViewModel::class.java)

        initLanguagesRv(selectLanguageViewModel)

        // change the text
        selectLanguageViewModel.firstLanguage.observe(viewLifecycleOwner, Observer {
            firstLanguage ->
            if(firstLanguage != null){
                choose_language_text.text = "Choose The Target Language"
            }
        })

        selectLanguageViewModel.secondLanguage.observe(viewLifecycleOwner, Observer {
            secondLanguage ->
            if(secondLanguage != null){
                select_language_button.isEnabled = true
                select_language_button.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.darkBlue));
            }
        })

        select_language_button.setOnClickListener {
            selectLanguageViewModel.saveCurrentUserLanguages()
            findNavController().navigate(SelectLanguageFragmentDirections.actionSelectLanguageToHomeFragment())
        }
    }

    private fun initLanguagesRv(selectLanguageViewModel: SelectLanguageViewModel) {
        val adapter = GroupAdapter<GroupieViewHolder>()
        select_language_rv.adapter = adapter
        select_language_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        for(languageType in Language.values()){
            adapter.add(LanguageItem(languageType.name, getLanguageIcon(languageType),
                languageType, selectLanguageViewModel::onLanguageItemSelected, requireContext()))
        }
    }

    companion object {
        fun getLanguageIcon(language: Language): Int{
            return when (language) {
                Language.Arabic -> R.drawable.ksa
                Language.English -> R.drawable.usa
                Language.France -> R.drawable.france
            }
        }
    }
}
