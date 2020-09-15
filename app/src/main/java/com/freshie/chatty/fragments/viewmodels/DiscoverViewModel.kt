package com.freshie.chatty.fragments.viewmodels

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.freshie.chatty.models.OnlineUser
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class DiscoverViewModel : ViewModel(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    var lastLocation: MutableLiveData<Location?> = MutableLiveData()

    private val _onlineUsers: MutableLiveData<MutableList<OnlineUser>> = MutableLiveData()
    val onlineUsers: LiveData<MutableList<OnlineUser>>
        get() = _onlineUsers

    private val _isMapReady = MutableLiveData<Boolean>()
    val isMapReady: LiveData<Boolean>
        get() = _isMapReady

    init {
        _onlineUsers.value = mutableListOf()
        getOnlineUsers()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(_map: GoogleMap?) {
        map = _map!!

        map.isMyLocationEnabled = false
        map.uiSettings.isCompassEnabled = false

        _isMapReady.value = true
    }

    @SuppressLint("MissingPermission")
    fun moveMapToLocation(latLng: LatLng) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
    }

    private fun getOnlineUsers(){
        val db = Firebase.firestore
        db.collection("online")
            .whereEqualTo("state", true)
            .addSnapshotListener {
                value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                val users = mutableListOf<OnlineUser>()
                for (doc in value!!) {
                    val onlineUser = doc.toObject<OnlineUser>()
                    users.add(onlineUser)
                }
                _onlineUsers.value =  users
            }
    }
}