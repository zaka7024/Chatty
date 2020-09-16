package com.freshie.chatty.fragments.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.location.Location
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.freshie.chatty.R
import com.freshie.chatty.fragments.SelectLanguageFragment.Companion.getLanguageIcon
import com.freshie.chatty.models.OnlineUser
import com.freshie.chatty.models.User
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*


class DiscoverViewModel(var context: Context) : ViewModel(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

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
        map.setOnInfoWindowClickListener(this)
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
        val db = Firebase.firestore
        val auth = Firebase.auth
        val docs = db.collection("online")
            .document("${auth.currentUser?.uid}")

        docs.update("lat", location.latitude)
        docs.update("lng", location.longitude)
    }

    private suspend fun getOnlineUsers() {
            val db = Firebase.firestore
            GlobalScope.launch {

            }
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
                        val user = (db.collection("users")
                            .whereEqualTo("id", onlineUser.id).limit(1).get()
                            .addOnSuccessListener {
                                val user = it.documents.first().toObject<User>()
                                val onlineMotherLanguage = user?.motherLanguage

                                val iconId = getLanguageIcon(onlineMotherLanguage!!)
                                var bitmap = BitmapFactory.decodeResource(context.resources, iconId)
                                bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false)

                                // Draw marker
                                val latlng = LatLng(onlineUser.lat, onlineUser.lng)
                                val marker = map.addMarker(
                                    MarkerOptions().position(latlng)
                                        .title(user.name)
                                        .snippet("Native: ${user.motherLanguage?.name}, Target: ${user.targetLanguage?.name}")
                                        .icon(
                                            BitmapDescriptorFactory.fromBitmap(bitmap)
                                        )
                                )
                                marker.tag = onlineUser
                                markers.add(marker)

                            })
                        users.add(onlineUser)
                    }
                    _onlineUsers.value = users
                }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.showInfoWindow()
        return false
    }

    override fun onInfoWindowClick(marker: Marker?) {
        // Decide to chat with this person
        val onlineUser = marker?.tag as OnlineUser
        onSelectPerson.value = Pair(true, onlineUser.id)
    }
}