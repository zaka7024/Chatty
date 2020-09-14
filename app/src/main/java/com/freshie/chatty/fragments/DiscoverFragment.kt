package com.freshie.chatty.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import com.freshie.chatty.R
import com.freshie.chatty.fragments.viewmodels.DiscoverViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_dicscover.*
import java.util.Observer

class DiscoverFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

        // Get location permission
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        discoverViewModel.isMapReady.observe(viewLifecycleOwner,  {isMapReady ->
            if(isMapReady) {
                if (checkLocationAccessPermission()) {

                }
            }
        })

        // Create the google map and show the view
        if(mapView != null){
            mapView.onCreate(null)
            mapView.getMapAsync(discoverViewModel)
            mapView.onResume()
        }
    }

    @SuppressLint("NewApi")
    fun checkLocationAccessPermission(): Boolean {
        if (activity?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            );
        }else {
            return true
        }
        return false
    }

}
