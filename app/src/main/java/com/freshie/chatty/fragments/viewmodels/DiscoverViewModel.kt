package com.freshie.chatty.fragments.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

class DiscoverViewModel : ViewModel(), OnMapReadyCallback {
    private lateinit var map: GoogleMap

    private val _isMapReady = MutableLiveData<Boolean>()
    val isMapReady: LiveData<Boolean>
        get() = _isMapReady

    @SuppressLint("MissingPermission")
    override fun onMapReady(_map: GoogleMap?) {
        map = _map!!

        map.isMyLocationEnabled = false
        map.uiSettings.isCompassEnabled = false

        _isMapReady.value = true
    }
}