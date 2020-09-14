package com.freshie.chatty.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.freshie.chatty.R
import com.freshie.chatty.fragments.viewmodels.DiscoverViewModel
import kotlinx.android.synthetic.main.fragment_dicscover.*
import java.util.Observer

class DiscoverFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val application = requireNotNull(this.activity).application

        return inflater.inflate(R.layout.fragment_dicscover, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val discoverViewModel = ViewModelProviders.of(this).get(DiscoverViewModel::class.java)

        discoverViewModel.isMapReady.observe(viewLifecycleOwner,  {isMapRead ->
            if(isMapRead) {
                // Do
            }
        })

        // Create the google map and show the view
        if(mapView != null){
            mapView.onCreate(null)
            mapView.getMapAsync(discoverViewModel)
            mapView.onResume()
        }
    }

}
