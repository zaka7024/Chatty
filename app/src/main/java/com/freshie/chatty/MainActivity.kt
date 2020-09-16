package com.freshie.chatty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.fxn.OnBubbleClickListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        navController = findNavController(R.id.myNavHostFragment)
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        Navigation.setViewNavController(bubbleTabBar, navController)
        setBubbleListeners()

        // set current user state
        saveUserOnlineState(true)
    }

    override fun onResume() {
        super.onResume()
        saveUserOnlineState(true)
    }

    override fun onStop() {
        super.onStop()
        saveUserOnlineState(false)
    }

    private fun saveUserOnlineState(state: Boolean) {
        val auth = Firebase.auth
        if(auth.uid != null){
            val db = Firebase.firestore
            db.collection("online")
                .document("${auth.uid}")
                .update("state", state)
        }
    }

    // navigate to main fragments
    private fun setBubbleListeners() {
        bubbleTabBar.addBubbleListener(object : OnBubbleClickListener {
            override fun onBubbleClick(id: Int) {
                when (id) {
                    R.id.nav_search -> {
                        navController.navigate(R.id.discoverFragment)
                    }
                    R.id.nav_home -> {
                        navController.navigate(R.id.homeFragment)
                    }
                    R.id.nav_profile -> {
                        navController.navigate(R.id.profileFragment)
                    }
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}
