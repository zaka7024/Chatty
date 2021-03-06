package com.freshie.chatty.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.freshie.chatty.R
import com.freshie.chatty.fragments.viewmodels.DiscoverViewModel
import com.freshie.chatty.fragments.viewmodels.DiscoverViewModelFactory
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_dicscover.*

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

        if (Firebase.auth.currentUser?.uid == null) {
            map_tip.visibility = View.VISIBLE
            map_tip_text.visibility = View.VISIBLE
            return
        }

        val discoverViewModelFactory  = DiscoverViewModelFactory(requireContext())
        val discoverViewModel = ViewModelProviders.of(this, discoverViewModelFactory)
            .get(DiscoverViewModel::class.java)

        // Get location permission
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        discoverViewModel.isMapReady.observe(viewLifecycleOwner,  {isMapReady ->
            if(isMapReady) {
                if (checkLocationAccessPermission()) {
                    moveCameraToLastPosition(discoverViewModel)
                }
            }
        })

        // Create the google map and show the view
        if(mapView != null){
            mapView.onCreate(null)
            mapView.getMapAsync(discoverViewModel)
            mapView.onResume()
        }

        // test
        discoverViewModel.onlineUsers.observe(viewLifecycleOwner, {
            if(it != null){
                Log.i("onlines", it.toString())
            }
        })

        discoverViewModel.onSelectPerson.observe(viewLifecycleOwner, {
            if(it.first) {
                findNavController().navigate(DiscoverFragmentDirections
                    .actionDiscoverFragmentToChatFragment(it.second))
            }
        })
    }


    @SuppressLint("MissingPermission")
    // Move the camera to the last user position
    private fun moveCameraToLastPosition(discoverViewModel: DiscoverViewModel) {
        // Get last user location
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if(location != null){
                val latLng = LatLng(location.latitude, location.longitude)
                discoverViewModel.moveMapToLocation(latLng)
                // Save last location
                discoverViewModel.onGetLastLocation(location)
            }
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
