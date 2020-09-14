package com.freshie.chatty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.fxn.OnBubbleClickListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        navController = findNavController(R.id.myNavHostFragment)
        Navigation.setViewNavController(bubbleTabBar, navController)
        setBubbleListeners()
    }

    // navigate to main fragments
    private fun setBubbleListeners() {
        bubbleTabBar.addBubbleListener(object : OnBubbleClickListener {
            override fun onBubbleClick(id: Int) {
                when(id){
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
}
