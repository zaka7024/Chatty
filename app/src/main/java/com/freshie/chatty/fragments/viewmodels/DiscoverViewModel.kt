package com.freshie.chatty.fragments.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.freshie.chatty.R
import com.freshie.chatty.models.OnlineUser
import com.freshie.chatty.models.User
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class DiscoverViewModel(var context: Context) : ViewModel(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    private lateinit var map: GoogleMap
    var lastLocation: MutableLiveData<Location?> = MutableLiveData()

    private val _onlineUsers: MutableLiveData<MutableList<OnlineUser>> = MutableLiveData()
    val onlineUsers: LiveData<MutableList<OnlineUser>>
        get() = _onlineUsers
    private val _isMapReady = MutableLiveData<Boolean>()

    val isMapReady: LiveData<Boolean>
        get() = _isMapReady

    var onSelectPerson: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()

    private val markers: MutableList<Marker> = mutableListOf()

    init {
        _onlineUsers.value = mutableListOf()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(_map: GoogleMap?) {
        map = _map!!

        map.isMyLocationEnabled = false
        map.setOnMarkerClickListener(this)
        map.uiSettings.isCompassEnabled = false
        _isMapReady.value = true

        scope.launch {
            withContext(Dispatchers.Main){
                getOnlineUsers()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun moveMapToLocation(latLng: LatLng) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8f))
        map.uiSettings.isZoomControlsEnabled = false
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
    }

    fun onGetLastLocation(location: Location) {
        saveCurrentUserLocation(location)
    }

    private fun saveCurrentUserLocation(location: Location) {
        // TODO:: SOLVE THIS PROBLEM
        val db = Firebase.firestore
        val auth = Firebase.auth
        val docs = db.collection("online")
            .document("${auth.currentUser?.uid}")
        docs.update("lat", location.latitude).addOnSuccessListener {
            Log.i("discover", "UPDATED")
        }.addOnFailureListener {
            Log.i("discover", "CAN NOT UPDATE")
        }

        docs.update("lng", location.longitude)
    }

    private suspend fun getOnlineUsers() {
            val db = Firebase.firestore
            db.collection("online")
                .whereEqualTo("state", true)
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    val users = mutableListOf<OnlineUser>()
                    markers.clear()
                    map.clear()
                    for (doc in value!!) {
                        val onlineUser = doc.toObject<OnlineUser>()

                        // Skip the current user
                        if (onlineUser.id == Firebase.auth.uid) continue

//                        // get user target language
//                        val user = (db.collection("users")
//                            .whereEqualTo("id", onlineUser.id).limit(1).get().await()
//                            .documents.first().toObject<User>())?.targetLanguage

                        // Draw marker
                        users.add(onlineUser)
                        val latlng = LatLng(onlineUser.lat, onlineUser.lng)
                        val marker = map.addMarker(
                            MarkerOptions().position(latlng)
                                .title("Person")
                        )
                        marker.tag = onlineUser
                        markers.add(marker)
                    }
                    _onlineUsers.value = users
                }

    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val onlineUser = marker?.tag as OnlineUser
        marker.showInfoWindow()

        // Decide to chat with this person
        onSelectPerson.value = Pair(true, onlineUser.id)

        return false
    }
}